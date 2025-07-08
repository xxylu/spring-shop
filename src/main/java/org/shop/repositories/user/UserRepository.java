package org.example.repositories.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.database.DatabaseConnection;
import org.example.models.user.Role;
import org.example.models.user.User;

public class UserRepository implements IUserRepository{

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String roleStr = rs.getString("role");
                Role role = null;
                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Unknown role: " + roleStr, e);
                }

                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .isActive(rs.getBoolean("isActive"))
                        .role(role)
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occuerd while reading database", e);
        }
        return users;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "select * from users where id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            if(rs.next()) {
                String roleStr = rs.getString("role");
                Role role = null;
                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Unknown role: " + roleStr, e);
                }

                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .isActive(rs.getBoolean("isActive"))
                        .role(role)
                        .build();
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error occuerd while reading database", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public Boolean isUserActive(String id) {
        return null;
    }
}
