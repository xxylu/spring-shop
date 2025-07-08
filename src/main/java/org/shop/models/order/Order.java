package org.shop.models.order;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String id;
    private String userId;
    private OrderStatus status;
    private String products;
}
