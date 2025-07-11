package org.shop.controllers;

import org.shop.dto.RegisterRequest;
import org.shop.models.Payment.Payment;
import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.services.payment.IPaymentUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.shop.models.product.Product;
import org.shop.repositories.cart.ICartRepository;
import org.shop.services.auth.IAuthService;
import org.shop.services.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    IAuthService authService;
    ICartRepository cartRepository;
    IProductService productService;
    IPaymentUpdateService paymentUpdateService;
    @Autowired
    public AdminController(
            IAuthService authService,
            ICartRepository cartRepository,
            IProductService productService,
            IPaymentUpdateService paymentUpdateService
    ) {
        this.authService = authService;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.paymentUpdateService = paymentUpdateService;
    }

    public static class UseridRequest {
        public String id;
    }

    @GetMapping("/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel() {
        return "Witaj w panelu administratora!";
    }

    @PostMapping("/addproduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addProduct(@RequestBody Product product, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("username: " + username);
        Optional<User> u=  authService.findbyLogin(username);
        System.out.println(u);
        if(u.isPresent()){
            productService.addProduct(
                    u.get().getUserid(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getCategory()
            );
            return ResponseEntity.ok().body("Dodano produkt do Bazy o id: " + product.getId());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bład");
    }

    @PostMapping("/removeproduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeVehicle(@RequestBody UseridRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u=  authService.findbyLogin(username);
        if(u.isPresent()) {
            productService.deleteProduct(u.get().getUserid(), request.id);
            return ResponseEntity.ok().body("usunięto produkt z bazy o id: " + request.id);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bład");
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        if(authService.register(
                registerRequest.getLogin(),
                registerRequest.getPassword(),
                Role.valueOf(registerRequest.getRole())
        )){
            return ResponseEntity.ok("Zarejestrowano ");
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/editpayment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> editPayment(@RequestBody Payment payment, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u=  authService.findbyLogin(username);
        if(u.isPresent()) {
            Optional<Payment> p = paymentUpdateService.getPaymentById(payment.getPaymentid());
            if(p.isPresent()) {
                paymentUpdateService.updatePayment(payment);
                return ResponseEntity.ok("Zmodyfikowano płatność o numerze: " +  payment.getPaymentid());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bład");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bład");
    }
}
