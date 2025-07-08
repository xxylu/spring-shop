package org.shop.services.product;

import org.springframework.stereotype.Service;
import org.shop.repositories.product.ProductRepository;

@Service
public class ProductService implements IProductService {
    private ProductRepository productRepository;
}