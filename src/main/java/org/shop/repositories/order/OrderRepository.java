package org.shop.repositories.order;

import org.shop.database.DatabaseConnection;
import org.shop.models.cart.Cart;
import org.shop.models.order.Order;
import org.shop.models.order.OrderStatus;
import org.shop.repositories.cart.CartRepository;
import org.shop.repositories.cart.ICartRepository;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class OrderRepository implements IOrderRepository {
    ICartRepository cartRepository = new CartRepository();


    @Override
    public Optional<Order> findById(String id) {
        String sql = "SELECT * FROM orders WHERE orderid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = Order.builder()
                            .orderid(rs.getString("orderid"))
                            .userId(rs.getString("userid"))
                            .status(OrderStatus.valueOf(rs.getString("status")))
                            .products(rs.getString("products"))
                            .build();
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET userid = ?, status = ?, products = ? WHERE orderid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, order.getUserId());
            stmt.setString(2, order.getStatus().toString());
            stmt.setString(3, order.getProducts());
            stmt.setString(4, order.getOrderid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    @Override
    public Optional<Order> findByUserId(String userId) {
        String sql = "SELECT * FROM orders WHERE userid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = Order.builder()
                            .orderid(rs.getString("orderid"))
                            .userId(rs.getString("userid"))
                            .status(OrderStatus.valueOf(rs.getString("status")))
                            .products(rs.getString("products"))
                            .build();
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by username", e);
        }
        return Optional.empty();
    }

    @Override
    public String createOrder(String userId, String cartId) {
        String orderId = UUID.randomUUID().toString();

        // Pobierz produkty z koszyka (zakładam, że masz tabelę carts z kolumną products)
        Optional<Cart> cart = cartRepository.findCartById(cartId);
        String productsJson = cart.get().getProducts();

        String sql = "INSERT INTO orders (orderid, userid, status, products) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, orderId);
            stmt.setString(2, userId);
            stmt.setString(3, OrderStatus.PENDING.toString()); // domyślny status
            stmt.setString(4, productsJson);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating order", e);
        }

        return orderId;
    }

    @Override
    public void deleteOrder(String orderId) {
        String sql = "DELETE FROM orders WHERE orderid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }
}
