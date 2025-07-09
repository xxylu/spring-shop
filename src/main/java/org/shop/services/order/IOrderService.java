package org.shop.services.order;

import org.shop.models.order.Order;

public interface IOrderService {
    void createOrder(String userId, String cartId);
    void updateStatus(String orderId, String userId, String status);
    void deleteOrder(String orderId, String userId);
    void addProductToOrder(String orderId, String userId, String productId);
    Boolean deleteProductFromOrder(String orderId, String userId, String productId);
    Boolean isBelongsToUser(String orderId, String userId);
}
