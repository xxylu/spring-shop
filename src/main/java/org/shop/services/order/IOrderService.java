package org.shop.services.order;

import org.shop.models.order.Order;

import java.util.Optional;

public interface IOrderService {
    void createOrder(String userId, String cartId);
    void updateStatus(String orderId, String userId, String status);
    void deleteOrder(String orderId, String userId);
    void addProductToOrder(String orderId, String userId, String productId);
    Boolean deleteProductFromOrder(String orderId, String userId, String productId);
    Boolean isBelongsToUser(String orderId, String userId);
    Optional<Order> findbyid(String orderId);
    Optional<Order> findbyUserId(String userId);
    void setStripeSesionId(String StripeId, String id);
    String getIdfromStripeid(String sessionId);
}
