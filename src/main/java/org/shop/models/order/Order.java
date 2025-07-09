package org.shop.models.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.shop.models.product.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String id;
    private String userId;
    private OrderStatus status;
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
