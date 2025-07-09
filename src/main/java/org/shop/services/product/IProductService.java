package org.shop.services.product;

import org.shop.models.product.Product;
import org.shop.models.product.ProductCategory;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    void addProduct(
            String adminid,
            String title,
            double price,
            String description,
            ProductCategory category
    );
    void deleteProduct(String adminid,String productid);
}
