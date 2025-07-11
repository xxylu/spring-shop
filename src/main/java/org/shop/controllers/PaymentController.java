package org.shop.controllers;

import org.shop.dto.PaymentRequest;
import org.shop.dto.PaymentResponse;
import org.shop.models.Payment.Payment;
import org.shop.models.Payment.PaymentStatus;
import org.shop.models.order.Order;
import org.shop.repositories.payment.PaymentRepository;
import org.shop.services.payment.PaymentService;
import org.shop.services.order.IOrderService;
import org.shop.services.payment.PaymentUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final IOrderService orderService;
    private final PaymentUpdateService paymentUpdateService;

    public PaymentController(
            PaymentService paymentService,
            IOrderService orderService,
            PaymentUpdateService paymentUpdateService
    ) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.paymentUpdateService = paymentUpdateService;
    }

    /**
     * Tworzy PaymentIntent dla zamówienia
     */
    @PostMapping("/intent/{orderId}")
    public ResponseEntity<PaymentResponse> createPaymentIntent(@PathVariable String orderId,
                                                               @RequestBody PaymentRequest paymentRequest) {
        Order order = orderService.findbyid(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zamówienie nie znalezione"));

        PaymentResponse response = paymentService.createPaymentIntent(paymentRequest, order);

        if ("success".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Tworzy checkout session (Stripe) dla zamówienia
     */
    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<PaymentResponse> createCheckoutSession(@PathVariable String orderId,
                                                                 @RequestBody PaymentRequest paymentRequest) {
        Order order = orderService.findbyid(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zamówienie nie znalezione"));

        PaymentResponse response = paymentService.createCheckoutSession(paymentRequest, order);

        if ("success".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Sprawdza status PaymentIntent
     */
    @GetMapping("/status/intent/{paymentIntentId}")
    public ResponseEntity<PaymentStatus> checkPaymentIntentStatus(@PathVariable String paymentIntentId) {
        PaymentStatus status = paymentService.checkPaymentStatus(paymentIntentId);
        return ResponseEntity.ok(status);
    }

    /**
     * Sprawdza status CheckoutSession
     */
    @GetMapping("/status/session/{sessionId}")
    public ResponseEntity<PaymentStatus> checkCheckoutSessionStatus(@PathVariable String sessionId) {
        PaymentStatus status = paymentService.checkCheckoutSessionStatus(sessionId);
        return ResponseEntity.ok(status);
    }

    /**
     * Endpoint do obsługi powrotu z checkout (success)
     */
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("session_id") String sessionId) {
        PaymentStatus status = paymentService.checkCheckoutSessionStatus(sessionId);

        if (status == PaymentStatus.PAID) {
            String order = orderService.getIdfromStripeid(sessionId);
            Optional<Order> orderObj = orderService.findbyid(order);
            orderService.updateStatus(orderObj.get().getOrderid(), orderObj.get().getUserId(), "PAID");
            List<Payment> p = paymentUpdateService.getPaymentsByUserId(orderObj.get().getUserId());
            Payment payment = p.get(0);
            payment.setPaymentStatus(PaymentStatus.PAID);
            paymentUpdateService.updatePayment(payment);
            return ResponseEntity.ok("Płatność zakończona sukcesem!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Płatność nie powiodła się.");
        }
    }

    /**
     * Endpoint do obsługi powrotu z checkout (anulowanie)
     */
    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancelled(@RequestParam("order_id") String orderId) {
        return ResponseEntity.ok("Płatność anulowana dla zamówienia: " + orderId);
    }
}
