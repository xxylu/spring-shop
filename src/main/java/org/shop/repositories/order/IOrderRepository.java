package org.shop.repositories.order;

import org.shop.models.order.Order;

import java.util.Optional;

public interface IOrderRepository {
    Optional<Order> findById(String id);
    void updateOrder(Order order);
    Optional<Order> findByUsername(String username);
    void addOrder(Order order);
    void deleteOrder(String orderId);

}
