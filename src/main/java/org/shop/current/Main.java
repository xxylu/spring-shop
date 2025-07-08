package org.shop.current;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
    }
}