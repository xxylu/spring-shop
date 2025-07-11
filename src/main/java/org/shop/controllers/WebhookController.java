package org.shop.controllers;

import com.stripe.model.checkout.Session;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;

import org.shop.services.order.IOrderService;
import org.shop.models.order.Order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

@RestController
@RequestMapping("/api/payment")
public class WebhookController {

    private final IOrderService orderService;

    // Służy do weryfikacji podpisu Stripe
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public WebhookController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/webhook")
    public String handleStripeWebhook(HttpServletRequest request) throws IOException {
        String payload = getBody(request);
        String sigHeader = request.getHeader("Stripe-Signature");

        Event event;
        try {
            // Weryfikujemy podpis z użyciem sekretu endpointu (ustawionego w panelu Stripe)
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            // Nieprawidłowy podpis – ignorujemy
            System.out.println("⚠️  Webhook signature verification failed.");
            return "";
        }

        // Pobieramy dane z eventu (bezpiecznie)
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        // Obsługa konkretnych typów zdarzeń
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().orElse(null);
                if (paymentIntent != null) {
                    String orderId = paymentIntent.getMetadata().get("orderId");
                    updateOrderStatus(orderId, "PAID");
                }
                break;

            case "checkout.session.completed":
                Session session = (Session) dataObjectDeserializer.getObject().orElse(null);
                if (session != null) {
                    String orderId = session.getMetadata().get("orderId");
                    updateOrderStatus(orderId, "PAID");
                }
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return "";
    }

    private void updateOrderStatus(String orderId, String status) {
        if (orderId != null) {
            Optional<Order> orderOpt = orderService.findbyid(orderId);
            if (orderOpt.isPresent()) {
                orderService.updateStatus(orderId, orderOpt.get().getUserId(), status);
                System.out.println("✅ Zaktualizowano status zamówienia " + orderId + " na " + status);
            } else {
                System.out.println("⚠️ Nie znaleziono zamówienia o ID: " + orderId);
            }
        } else {
            System.out.println("⚠️ Brak orderId w metadanych");
        }
    }

    private String getBody(HttpServletRequest request) throws IOException {
        try (Scanner s = new Scanner(request.getInputStream(), "UTF-8")) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }
}
