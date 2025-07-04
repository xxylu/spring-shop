package org.example.models.cart;


import lombok.*;
import org.example.models.product.Product;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private String id;
    private List<Product> products;
}
