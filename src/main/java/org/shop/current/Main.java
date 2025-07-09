package org.shop.current;

import org.shop.models.user.*;
import org.shop.models.product.*;
import org.shop.repositories.cart.CartRepository;
import org.shop.services.authentication.AuthService;
import org.shop.services.authentication.IAuthService;
import org.shop.services.cart.CartService;
import org.shop.services.cart.ICartService;
import org.shop.services.order.IOrderService;
import org.shop.services.order.OrderService;
import org.shop.services.product.IProductService;
import org.shop.services.product.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Optional;

@ComponentScan(basePackages = {
        "org.shop.controllers",
        "org.shop.services",
        "org.shop.repositories",
        "org.shop.models",
        "org.shop.security"
})


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);



//        IAuthService authService = new AuthService();
//        ICartService cartService = new CartService();
//        IOrderService orderService = new OrderService();
//
//        authService.register("admin2", "123", Role.ADMIN);
//        Optional<User> u = authService.login("admin2", "123");
//        String cart = cartService.createCart(u.get().getUserid());
//        cartService.addToCart(u.get().getUserid(),cartService.getCartId(u.get().getUserid()), "fcfc0da5-53fd-4213-bd11-2c9d889987f0");
//        cartService.addToCart(u.get().getUserid(),cartService.getCartId(u.get().getUserid()), "fcfc0da5-53fd-4213-bd11-2c9d889987f0");
//        cartService.addToCart(u.get().getUserid(),cartService.getCartId(u.get().getUserid()), "fcfc0da5-53fd-4213-bd11-2c9d889987f0");
//
//        orderService.createOrder(u.get().getUserid(), cart);
    }


    private static void CartService() {
        IProductService productService = new ProductService();
        ICartService cartService = new CartService();
        System.out.println(productService.getAllProducts());
        IAuthService authService = new AuthService();
        Optional<User> u = authService.login("admin2", "123");
        String cart = cartService.createCart(u.get().getUserid());
        cartService.addToCart(u.get().getUserid(), cartService.getCartId(u.get().getUserid()), "fcfc0da5-53fd-4213-bd11-2c9d889987f0");
        cartService.removeFromCart(u.get().getUserid(),cartService.getCartId(u.get().getUserid()), "fcfc0da5-53fd-4213-bd11-2c9d889987f0");
        cartService.clearCart(cartService.getCartId(u.get().getUserid()));
        cartService.deleteCart(u.get().getUserid());
    }
    private static void AuthService() {
        IAuthService authService = new AuthService();
        authService.register("admin2", "123", Role.ADMIN);
        Optional<User> u = authService.login("admin2", "123");
        authService.register("user", "1234", Role.USER);
        Optional<User> u2 = authService.login("user", "1234");
        authService.activateUser(u2.get().getUserid(), u2.get().getUserid());
        authService.deleteUser(u.get().getUserid(), u2.get().getUserid());
    }

    private static void ProductService() {
        IProductService productService = new ProductService();
        IAuthService authService = new AuthService();
        //AuthService(); // test AuthService
        Optional<User> u = authService.login("admin2", "123");
        productService.addProduct(u.get().getUserid(), "Iphone 15 pro max", 600000, "Nowy telefon Iphone 14 pro max", ProductCategory.Electronics);
        System.out.println(productService.getAllProducts());
    }
}