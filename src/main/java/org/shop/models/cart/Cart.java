package org.shop.models.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private String cartid;
    private String userId;
    private String products;

    public void convertToJson(List<String> productIds) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.products = objectMapper.writeValueAsString(productIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<String> convertFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.products, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
