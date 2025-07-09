package org.shop.repositories.cart;

import org.shop.database.DatabaseConnection;
import org.shop.models.cart.Cart;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CartRepository implements ICartRepository {

    @Override
    public Optional<Cart> findCartById(String cartId) {
        String sql = "SELECT * FROM carts WHERE cartid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = Cart.builder()
                            .cartid(rs.getString("cartid"))
                            .userId(rs.getString("userid"))
                            .products(rs.getString("products"))
                            .build();
                    return Optional.of(cart);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cart by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Cart> findCartByUserId(String userId) {
        String sql = "SELECT * FROM carts WHERE userid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = Cart.builder()
                            .cartid(rs.getString("cartid"))
                            .userId(rs.getString("userid"))
                            .products(rs.getString("products"))
                            .build();
                    return Optional.of(cart);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cart by user ID", e);
        }
        return Optional.empty();
    }


    @Override
    public void modifyCart(String cartId, List<String> productIds) {
        Cart cart = findCartById(cartId).orElseThrow(() ->
                new RuntimeException("Cart not found with id: " + cartId));

        cart.convertToJson(productIds);

        String sql = "UPDATE carts SET products = ? WHERE cartid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, cart.getProducts());
            stmt.setString(2, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error modifying cart", e);
        }
    }

    @Override
    public String createCart(String userId) {
        String cartId = UUID.randomUUID().toString();
        String sql = "INSERT INTO carts (cartid, userid, products) VALUES (?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, cartId);
            stmt.setString(2, userId);
            stmt.setString(3, "[]");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating cart", e);
        }

        return cartId;
    }

    @Override
    public void deleteCart(String userId) {
        String sql = "DELETE FROM carts WHERE userid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting cart", e);
        }
    }
}
