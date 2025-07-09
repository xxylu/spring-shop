package org.shop.controllers;

import org.shop.dto.LoginRequest;
import org.shop.dto.RegisterRequest;
import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.services.authentication.AuthService;
import org.shop.services.authentication.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    IAuthService authService = new AuthService();

//    curl -X POST http://localhost:8080/auth/login \
//            -H "Content-Type: application/json" \
//            -d '{
//            "login": "admin2",
//            "password": "123"
//     }'
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Optional<User> u = authService.login(request.getLogin(), request.getPassword());
        if (u.isPresent()) {
            return ResponseEntity.ok("Zalogowano pomyślnie");
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
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

    @PostMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }
}
