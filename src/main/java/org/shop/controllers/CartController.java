package org.shop.controllers;

import org.shop.models.cart.Cart;
import org.shop.models.user.User;
import org.shop.services.auth.AuthService;
import org.shop.services.cart.CartService;
import org.shop.services.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final AuthService authService;
    CartService cartService;
    ProductService productService;

    public CartController(CartService cartService, ProductService productService, AuthService authService) {
        this.cartService = cartService;
        this.productService = productService;
        this.authService = authService;
    }

    public static class CartRequest {
        public String productid;
    }

    /*
    curl -X POST http://localhost:8080/api/cart/createcart \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer "

     */

    @PostMapping("/createcart")
    public ResponseEntity<String> createCart(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("username: " + username);
        Optional<User> u = authService.findbyLogin(username);
        System.out.println(u);
        if (u.isPresent()) {
            String id = cartService.createCart(u.get().getUserid());
            return ResponseEntity.ok().body("Utworzono koszyk \nid: "+ id);
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }


    /*
    curl -X POST http://localhost:8080/api/cart/deletecart \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer "

    */

    @DeleteMapping("/deletecart")
    public ResponseEntity<String> deleteCart(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u = authService.findbyLogin(username);
        if (u.isPresent()) {
            cartService.deleteCart(u.get().getUserid());
            return ResponseEntity.ok().body("Usunięto koszyk");
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }


    /*
        curl -X GET http://localhost:8080/api/cart/addtocart \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer " \
 -d '{
   "productid":"id"
 }'
     */
    @PostMapping("/addtocart")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest cartRequest, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u = authService.findbyLogin(username);
        if (u.isPresent()) {
            String userId = u.get().getUserid();
            String cartId = cartService.getCartId(userId);
            cartService.addToCart(userId,cartId,cartRequest.productid);
            Optional<Cart> cart = cartService.getCart(userId);
            List<String> products = cart.get().convertFromJson();
            return ResponseEntity.ok().body("Dodano produkt do koszyka\n obecny rozmiar: " + products.size() + "\n\n\n" + products);
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }

    /*
          curl -X GET http://localhost:8080/api/cart/removefromcart \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer " \
 -d '{
   "productid":"id"
 }'
     */
    @DeleteMapping("/removefromcart")
    public ResponseEntity<String> removeFromCart(@RequestBody CartRequest cartRequest, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> u = authService.findbyLogin(username);
        if (u.isPresent()) {
            String userId = u.get().getUserid();
            String cartId = cartService.getCartId(userId);
            cartService.removeFromCart(userId,cartId,cartRequest.productid);
            Optional<Cart> cart = cartService.getCart(userId);
            List<String> products = cart.get().convertFromJson();
            return ResponseEntity.ok().body("Usunięto produkt do koszyka\n obecny rozmiar: " + products.size()  + "\n\n\n" + products);
        }
        return ResponseEntity.status(401).body("Błąd, user nie istnieje");
    }


}
