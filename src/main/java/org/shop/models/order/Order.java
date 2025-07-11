package org.shop.models.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.shop.models.Payment.PaymentStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String orderid;
    private String userId;
    private OrderStatus status;
    private String products;  // JSON z listą Stringów (ID produktów)
    private String paymentStatus;

    // Zapisuje listę ID produktów jako JSON
    public void convertToJson(List<String> productIds) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.products = objectMapper.writeValueAsString(productIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Odczytuje listę ID produktów z JSON
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
