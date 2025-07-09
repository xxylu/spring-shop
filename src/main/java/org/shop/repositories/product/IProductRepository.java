package org.shop.repositories.product;

import org.shop.models.product.Product;

import java.util.List;

public interface IProductRepository {
    Product findById(String id);
    Product findByName(String name);
    List<Product> findAll();
    void deleteById(String id);
    void addProduct(Product product);
}
