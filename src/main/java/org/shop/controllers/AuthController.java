package org.shop.controllers;

import org.shop.dto.LoginRequest;
import org.shop.models.product.Product;
import org.shop.models.user.Role;
import org.shop.services.auth.IAuthService;
import org.shop.services.jwt.JWTService;
import org.shop.services.product.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    IAuthService authService;
    IProductService productService;
    JWTService jwtService;


    public AuthController(
            IAuthService authService,
            JWTService jwtService,
            IProductService productService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.productService = productService;
    }

//    curl -X POST http://localhost:8080/auth/login \
//            -H "Content-Type: application/json" \
//            -d '{
//            "login": "admin2",
//            "password": "123"
//     }'
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        if (authService.userExists(request.getLogin())) {
            return authService.login(request.getLogin(), request.getPassword())
                    .map(jwtService::generateToken)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(401).body("Invalid credentials"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest registerRequest) {
         if(authService.register(
                registerRequest.getLogin(),
                registerRequest.getPassword(),
                Role.valueOf("USER")
         )){
             return ResponseEntity.ok("Zarejestrowano ");
         }
         return ResponseEntity.badRequest().body("User już istnieje");
    }

    @PostMapping("/getallproducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }
}
