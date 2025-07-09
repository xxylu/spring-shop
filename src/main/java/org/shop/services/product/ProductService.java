package org.shop.services.product;

import org.shop.models.product.Product;
import org.shop.models.product.ProductCategory;
import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.user.UserRepository;
import org.shop.services.authentication.AuthService;
import org.springframework.stereotype.Service;
import org.shop.repositories.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository =  new ProductRepository();
    private final UserRepository userRepository =  new UserRepository();

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(String adminid, String title, double price, String description, ProductCategory category) {
        Optional<User> u = userRepository.findById(adminid);
        if(u.isPresent() && u.get().getRoles() == Role.ADMIN) {
            Product product = Product.builder()
                    .id(UUID.randomUUID().toString())
                    .title(title)
                    .price(price)
                    .description(description)
                    .category(category)
                    .isactive(true)
                    .build();
            System.out.println("Dodano produkt do bazy: " + title);
            productRepository.addProduct(product);
            return;
        }
        System.out.println("Brak uprawnień");
    }

    @Override
    public void deleteProduct(String adminid, String productid) {
        Optional<User> u = userRepository.findById(adminid);
        if(u.isPresent() && u.get().getRoles() == Role.ADMIN) {
            productRepository.deleteById(productid);
            System.out.println("Usunięto produkt z bazy (isActive = false)");
        }
    }
}