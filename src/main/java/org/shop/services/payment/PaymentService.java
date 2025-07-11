package org.shop.services.payment;

import jakarta.annotation.PostConstruct;
import org.shop.dto.PaymentRequest;
import org.shop.dto.PaymentResponse;
import org.shop.models.Payment.PaymentStatus;
import org.shop.models.order.Order;
import org.shop.models.product.Product;
import org.shop.services.order.IOrderService;
import org.shop.services.product.IProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentService {

    private final IProductService productService;
    private final IOrderService orderService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    public PaymentService(IProductService productService, IOrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    /**
     * Ustawiamy klucz Stripe po uruchomieniu serwisu
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * Obliczanie całkowitej kwoty zamówienia w złotówkach
     */
    public BigDecimal calculateOrderTotal(Order order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> productIds = objectMapper.readValue(order.getProducts(), new TypeReference<List<String>>() {});
            BigDecimal total = BigDecimal.ZERO;

            for (String productId : productIds) {
                Optional<Product> p = productService.findById(productId);
                if (p.isEmpty()) {
                    throw new IllegalArgumentException("Produkt nie znaleziony: " + productId);
                }
                total = total.add(BigDecimal.valueOf(p.get().getPrice()));
            }

            return total;
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy obliczaniu całkowitej ceny zamówienia", e);
        }
    }

    /**
     * Tworzy PaymentIntent (do płatności kartą itp.)
     */
    public PaymentResponse createPaymentIntent(PaymentRequest paymentRequest, Order order) {
        try {
            long amountInCents = calculateOrderTotal(order)
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(paymentRequest.getCurrency().toLowerCase())
                    .setDescription("Zamówienie nr " + order.getOrderid())
                    .putMetadata("order_id", order.getOrderid())
                    .putMetadata("user_email", paymentRequest.getEmail())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            PaymentResponse response = new PaymentResponse("success", "Utworzono PaymentIntent.");
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setPaymentIntentId(paymentIntent.getId());
            response.setAmount(amountInCents);
            return response;

        } catch (StripeException e) {
            return new PaymentResponse("error", "Błąd Stripe: " + e.getMessage());
        }
    }

    /**
     * Tworzy checkout session (płatność przez stronę Stripe)
     */
    public PaymentResponse createCheckoutSession(PaymentRequest paymentRequest, Order order) {
        try {
            long amountInCents = calculateOrderTotal(order)
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValue();

            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(baseUrl + "/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(baseUrl + "/api/payments/cancel?order_id=" + order.getOrderid())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(paymentRequest.getCurrency().toLowerCase())
                                    .setUnitAmount(amountInCents)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Zamówienie ID: " + order.getOrderid())
                                            .build())
                                    .build())
                            .build()
                    )
                    .putMetadata("order_id", order.getOrderid())
                    .putMetadata("user_email", paymentRequest.getEmail());

            if (paymentRequest.getEmail() != null && !paymentRequest.getEmail().isEmpty()) {
                builder.setCustomerEmail(paymentRequest.getEmail());
            }

            Session session = Session.create(builder.build());

            // Aktualizacja zamówienia w bazie
            orderService.setStripeSesionId(session.getId(), order.getOrderid());
            orderService.updateStatus(order.getOrderid(), order.getUserId(), "PENDING");

            PaymentResponse response = new PaymentResponse("success", "Utworzono checkout session.");
            response.setCheckoutUrl(session.getUrl());
            response.setUrl(session.getUrl());
            response.setPaymentIntentId(session.getId());
            response.setAmount(amountInCents);
            return response;

        } catch (StripeException e) {
            return new PaymentResponse("error", "Błąd Stripe: " + e.getMessage());
        }
    }

    /**
     * Sprawdzanie statusu PaymentIntent
     */
    public PaymentStatus checkPaymentStatus(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            switch (paymentIntent.getStatus()) {
                case "succeeded":
                    return PaymentStatus.PAID;
                case "processing":
                case "requires_payment_method":
                case "requires_confirmation":
                case "requires_action":
                    return PaymentStatus.PENDING;
                case "canceled":
                case "requires_capture":
                    return PaymentStatus.FAILED;
                default:
                    return PaymentStatus.PENDING;
            }
        } catch (StripeException e) {
            return PaymentStatus.FAILED;
        }
    }

    /**
     * Sprawdzanie statusu checkout session
     */
    public PaymentStatus checkCheckoutSessionStatus(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            if ("complete".equals(session.getStatus()) && "paid".equals(session.getPaymentStatus())) {
                return PaymentStatus.PAID;
            } else if ("open".equals(session.getStatus())) {
                return PaymentStatus.PENDING;
            } else {
                return PaymentStatus.FAILED;
            }
        } catch (StripeException e) {
            return PaymentStatus.FAILED;
        }
    }

    public void updatePaymentStatus(Order order, PaymentStatus status) {
        order.setPaymentStatus(status.toString());
    }

    public boolean isPaymentCompleted(Order order) {
        return Objects.equals(order.getPaymentStatus(), PaymentStatus.PAID.toString());
    }

    public boolean isPaymentPending(Order order) {
        return Objects.equals(order.getPaymentStatus(), PaymentStatus.PENDING.toString());
    }
}
