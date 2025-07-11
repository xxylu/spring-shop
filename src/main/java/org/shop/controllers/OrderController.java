package org.shop.controllers;

import org.shop.dto.PaymentRequest;
import org.shop.dto.PaymentResponse;
import org.shop.models.Payment.Payment;
import org.shop.models.Payment.PaymentStatus;
import org.shop.models.order.Order;
import org.shop.models.user.User;
import org.shop.services.payment.PaymentService;
import org.shop.services.auth.IAuthService;
import org.shop.services.cart.ICartService;
import org.shop.services.order.IOrderService;
import org.shop.services.payment.PaymentUpdateService;
import org.shop.services.product.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    IAuthService authService;
    IOrderService orderService;
    ICartService cartService;
    IProductService productService;
    PaymentService paymentService;
    PaymentUpdateService paymentUpdateService;

    public OrderController(
            IOrderService orderService,
            IAuthService authService,
            ICartService cartService,
            IProductService productService,
            PaymentService paymentService,
            PaymentUpdateService paymentUpdateService
    ) {
        this.orderService = orderService;
        this.authService = authService;
        this.cartService = cartService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.paymentUpdateService = paymentUpdateService;
    }

    public static class OrderRequest {
        public String orderId;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = authService.findbyLogin(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Błąd, user nie istnieje");
        }

        User user = userOpt.get();
        String userId = user.getUserid();
        String cartId = cartService.getCartId(userId);

        try {
            orderService.createOrder(userId, cartId);
            Optional<Order> orderOpt = orderService.findbyUserId(userId);

            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Nie znaleziono zamówienia");
            }

            Order order = orderOpt.get();
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(order.getOrderid());
            paymentRequest.setUserId(userId);
            paymentRequest.setCurrency("pln");  // ustaw walutę
            //paymentRequest.setEmail(user.getEmail());  // ustaw email

            PaymentResponse paymentResponse = paymentService.createCheckoutSession(paymentRequest, order);

            if ("success".equals(paymentResponse.getStatus())) {
                Payment p = Payment.builder()
                        .paymentid(UUID.randomUUID().toString())
                        .orderid(order.getOrderid())
                        .userid(userId)
                        .paymentStatus(PaymentStatus.PENDING)
                        .build();
                paymentUpdateService.createPayment(p);
                return ResponseEntity.ok("Utworzono zamówienie: " + order.getOrderid() + "\n" + paymentResponse.getCheckoutUrl());
            } else {
                return ResponseEntity.status(500).body("Błąd przy tworzeniu płatności: " + paymentResponse.getMessage());
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił błąd: " + e.getMessage());
        }
    }


    @DeleteMapping("/destroy")
    public ResponseEntity<String> destroyOrder(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u = authService.findbyLogin(username);
        if (u.isPresent()) {
            String userId = u.get().getUserid();
            Optional<Order> o = orderService.findbyUserId(userId);
            if (o.isPresent()) {
                orderService.deleteOrder(o.get().getOrderid(), userId);
                return ResponseEntity.ok().body("Usunięto zamówienie");
            }
            return ResponseEntity.status(401).body("Błąd, zamówienie nie istnieje");
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getOrder(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u = authService.findbyLogin(username);
        if (u.isPresent()) {
            String userId = u.get().getUserid();
            Optional<Order> o = orderService.findbyUserId(userId);
            if (o.isPresent()) {
                String orderId = o.get().getOrderid();
                return ResponseEntity.ok().body(orderId);
            }
            return ResponseEntity.status(401).body("Błąd, zamówienie nie istnieje");
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }
}
