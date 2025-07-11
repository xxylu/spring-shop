package org.shop.services.order;

import org.shop.models.order.Order;
import org.shop.models.order.OrderStatus;
import org.shop.repositories.cart.ICartRepository;
import org.shop.repositories.order.IOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;

    public OrderService(IOrderRepository orderRepository, ICartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void createOrder(String userId, String cartId) {
        if (cartRepository.findCartById(cartId).isPresent()) {
            orderRepository.createOrder(userId, cartId);
            System.out.println("Utworzono zamówienie");
        } else {
            System.out.println("Błąd w tworzeniu zamówienia");
        }
    }

    @Override
    public void updateStatus(String orderId, String userId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent() && isBelongsToUser(orderId, userId)) {
            Order order = orderOpt.get();
            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.updateOrder(order);
            System.out.println("Zaktualizowano zamówienie");
        } else {
            System.out.println("Brak uprawnień do modyfikacji zamówienia");
        }
    }

    @Override
    public void deleteOrder(String orderId, String userId) {
        if (isBelongsToUser(orderId, userId)) {
            orderRepository.deleteOrder(orderId);
            System.out.println("Usunięto zamówienie");
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
        return orderRepository.findById(orderId)
                .map(order -> order.getUserId().equals(userId))
                .orElse(false);
    }

    @Override
    public Optional<Order> findbyid(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findbyUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void setStripeSesionId(String id, String orderId) {
        orderRepository.setStripeSesionId(id, orderId);
    }

    @Override
    public String getIdfromStripeid(String sessionId) {
        return orderRepository.getIdFromStripeSessionId(sessionId);
    }
}
