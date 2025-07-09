package org.shop.services.order;


import org.shop.models.order.Order;
import org.shop.models.product.Product;
import org.shop.repositories.order.IOrderRepository;
import org.shop.repositories.order.OrderRepository;
import org.shop.repositories.product.IProductRepository;
import org.shop.repositories.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService implements IOrderService {
    IProductRepository productRepository = new ProductRepository();
    IOrderRepository orderRepository = new OrderRepository();

    @Override
    public void updateOrder(Order order, String userId) {
        if(isBelongsToUser(order.getUserId(), userId)) {
            orderRepository.updateOrder(order);
        }
    }

    @Override
    public void deleteOrder(String orderId, String userId) {
        if(isBelongsToUser(orderId, userId)) {
            orderRepository.deleteOrder(orderId);
        }
    }

    @Override
    public void addProductToOrder(String orderId, String userId, Product product) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent() && isBelongsToUser(orderId, userId)) {
            List<Product> list = order.get().convertFromJson();
            list.add(product);
            order.get().convertToJson(list);
            orderRepository.updateOrder(order.get());
        }
    }

    @Override
    public Boolean deleteProductFromOrder(String orderId, String userId, Product product) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent() && isBelongsToUser(orderId, userId)) {
            List<Product> list = order.get().convertFromJson();
            boolean removed = list.remove(product);
            if (removed) {
                order.get().convertToJson(list);
                orderRepository.updateOrder(order.get());
            }
            return removed;
        }
        return false;
    }

    @Override
    public Boolean isBelongsToUser(String orderId, String userId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(value -> value.getUserId().equals(userId)).orElse(false);
    }
}
