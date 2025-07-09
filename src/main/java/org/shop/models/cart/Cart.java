package org.shop.models.cart;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.shop.models.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private String id;
    private String userId;
    private String products;


    public void convertToJson(List<Product> products) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.products = objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Product> convertFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Product> productList = objectMapper.readValue(
                    this.products,
                    new TypeReference<List<Product>>() {}
            );
            return productList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
