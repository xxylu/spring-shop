package org.example.models.cart;


import lombok.*;
import java.util.List;
import org.example.models.product.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private String id;
    private String userId;
    private String products;


    public void convertToJson(List<Product> products) {
        String json = "";

        for (Product product : products) {

        }

        this.products = json;
    }
}
