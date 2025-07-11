package org.shop.current;

import org.shop.models.user.*;
import org.shop.models.product.*;
import org.shop.services.authentication.AuthService;
import org.shop.services.authentication.IAuthService;
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
//        IProductService productService = new ProductService();
//        IAuthService authService = new AuthService();
//        authService.register("admin2", "123", Role.ADMIN);
//        Optional<User> u = authService.login("admin2", "123");
//        productService.addProduct(u.get().getUserid(), "Iphone 15 pro max", 600000, "Nowy telefon Iphone 15 pro max", ProductCategory.Electronics);
//        productService.addProduct(u.get().getUserid(), "Samsung Galaxy S24 Ultra", 550000, "Flagowy smartfon Samsunga z doskonałym aparatem", ProductCategory.Electronics);
//        productService.addProduct(u.get().getUserid(), "PlayStation 5", 250000, "Konsola najnowszej generacji od Sony", ProductCategory.Gaming);
//        productService.addProduct(u.get().getUserid(), "Dell XPS 13", 450000, "Lekki laptop z ekranem 4K", ProductCategory.Computers);
//        productService.addProduct(u.get().getUserid(), "Apple Watch Series 9", 150000, "Nowoczesny smartwatch od Apple", ProductCategory.Wearables);
//        productService.addProduct(u.get().getUserid(), "GoPro Hero 12", 120000, "Kamera sportowa o wysokiej rozdzielczości", ProductCategory.Photography);
//        productService.addProduct(u.get().getUserid(), "Logitech MX Master 3S", 40000, "Profesjonalna mysz bezprzewodowa", ProductCategory.Accessories);
//        productService.addProduct(u.get().getUserid(), "Nintendo Switch OLED", 180000, "Konsola przenośna z ekranem OLED", ProductCategory.Gaming);
//        productService.addProduct(u.get().getUserid(), "Canon EOS R7", 680000, "Bezlusterkowy aparat cyfrowy dla entuzjastów", ProductCategory.Photography);
//        productService.addProduct(u.get().getUserid(), "JBL Charge 5", 70000, "Wodoodporny głośnik Bluetooth o dużej mocy", ProductCategory.Audio);

        SpringApplication.run(Main.class, args);
    }
}