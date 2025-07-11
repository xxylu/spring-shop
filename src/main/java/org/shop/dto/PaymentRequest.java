package org.shop.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String userId;
    private String currency = "PLN";
    private String email;
}
