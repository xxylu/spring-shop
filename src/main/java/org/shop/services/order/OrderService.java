package org.shop.services.order;

import org.shop.models.order.Order;
import org.shop.models.order.OrderStatus;
import org.shop.models.product.Product;
import org.shop.repositories.cart.CartRepository;
import org.shop.repositories.cart.ICartRepository;
import org.shop.repositories.order.IOrderRepository;
import org.shop.repositories.order.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderService implements IOrderService {
    IOrderRepository orderRepository = new OrderRepository();
    ICartRepository cartRepository = new CartRepository();

    @Override
    public void createOrder(String userId, String cartId) {
        if (cartRepository.findCartById(cartId).isPresent()) {
            System.out.println("utworzono zamówienie");
            orderRepository.createOrder(userId, cartId);
            return;
        }
        System.out.println("Błąd w tworzeniu zamówienia");
    }

    @Override
    public void updateStatus(String orderId, String userId, String status) {
        Optional<Order> o = orderRepository.findById(orderId);
        if (isBelongsToUser(orderId, userId) && o.isPresent()) {
            o.get().setStatus(OrderStatus.valueOf(status));
            orderRepository.updateOrder(o.get());
            System.out.println("Zaaktualizowano zamówienie");
        }
        System.out.println("brak uprawnień do modyfikacji zamówienia");
    }

    @Override
    public void deleteOrder(String orderId, String userId) {

        if (isBelongsToUser(orderId, userId)) {
            orderRepository.deleteOrder(orderId);
        }
    }

    @Override
    public void addProductToOrder(String orderId, String userId, String productId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent() && isBelongsToUser(orderId, userId)) {
            Order order = orderOpt.get();

            List<String> productIds = order.convertFromJson();
            productIds.add(productId);

            order.convertToJson(productIds);
            orderRepository.updateOrder(order);
        }
    }

    @Override
    public Boolean deleteProductFromOrder(String orderId, String userId, String productId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent() && isBelongsToUser(orderId, userId)) {
            Order order = orderOpt.get();

            List<String> productIds = order.convertFromJson();
            boolean removed = productIds.remove(productId);
            if (removed) {
                order.convertToJson(productIds);
                orderRepository.updateOrder(order);
            }
            return removed;
        }
        return false;
    }

    @Override
    public Boolean isBelongsToUser(String orderId, String userId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        return orderOpt.map(order -> order.getUserId().equals(userId)).orElse(false);
    }
}
