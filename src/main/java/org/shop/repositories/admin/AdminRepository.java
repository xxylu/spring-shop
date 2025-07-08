package org.shop.repositories.admin;

import org.shop.database.DatabaseConnection;
import org.shop.models.user.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminRepository implements IAdminRepository {

    @Override
    public void DeleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting user", e);
        }
    }

    @Override
    public void BanUser(String userId) {
        String sql = "UPDATE users SET isActive = false WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while banning user", e);
        }
    }

    @Override
    public void DeleteOrder(String orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting order", e);
        }
    }

    @Override
    public void PromoteUserToAdmin(String userId) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, Role.ADMIN.name());
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while promoting user", e);
        }
    }
}
