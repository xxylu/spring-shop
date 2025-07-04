package org.example.models.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String id;
    private String name;
    private String description;
    private ProductCategory category;
    private Boolean isactive;
}
