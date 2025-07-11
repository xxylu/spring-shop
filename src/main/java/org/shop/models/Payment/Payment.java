package org.shop.models.Payment;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    private String paymentid;
    private String orderid;
    private String userid;
    private PaymentStatus paymentStatus;
}
