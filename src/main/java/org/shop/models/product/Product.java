package org.shop.models.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String id;
    private String title;
    private double price;
    private String description;
    private ProductCategory category;
    private Boolean isactive;
}
