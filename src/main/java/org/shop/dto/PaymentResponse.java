package org.shop.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String status;         // np. "success" lub "error"
    private String message;        // dodatkowy opis
    private String clientSecret;   // dla PaymentIntent
    private String paymentIntentId; // lub checkout session ID
    private String checkoutUrl;    // dla checkout
    private String url;
    private Long amount;           // kwota w centach

    public PaymentResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
