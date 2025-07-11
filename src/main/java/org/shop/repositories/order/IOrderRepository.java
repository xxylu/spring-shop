package org.shop.repositories.order;

import org.shop.models.order.Order;

import java.util.Optional;

public interface IOrderRepository {
    Optional<Order> findById(String id);
    void updateOrder(Order order);
    Optional<Order> findByUserId(String username);
    String createOrder(String userId, String CartId);
    void deleteOrder(String orderId);
    void setStripeSesionId(String id, String orderId);
    String getIdFromStripeSessionId(String sessionId);
}
