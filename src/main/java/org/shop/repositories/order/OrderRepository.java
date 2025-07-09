package org.shop.repositories.order;

import org.shop.models.order.Order;

import java.util.Optional;

public class OrderRepository implements IOrderRepository {

    @Override
    public Optional<Order> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public Optional<Order> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void addOrder(Order order) {

    }

    @Override
    public void deleteOrder(String orderId) {

    }
}
