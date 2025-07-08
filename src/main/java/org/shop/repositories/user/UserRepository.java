package org.shop.repositories.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.shop.database.DatabaseConnection;
import org.shop.models.user.Role;
import org.shop.models.user.User;

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
        String sql = "SELECT * FROM users WHERE login = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleStr = rs.getString("role");
                Role role = null;
                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Unknown role: " + roleStr, e);
                }

                return User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .isActive(rs.getBoolean("isActive"))
                        .role(role)
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while querying user by username", e);
        }
    }

    @Override
    public Boolean isUserActive(String id) {
        String sql = "SELECT isActive FROM users WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("isActive");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while checking user activity", e);
        }
    }

    @Override
    public boolean userExists(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while checking if user exists", e);
        }
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (id, login, password, role, isActive) VALUES (?, ?, ?, ?, ?)";

        if (userExists(user.getLogin())) {
            throw new RuntimeException("User already exists");
        }

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setBoolean(5, user.getIsActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while adding user", e);
        }
    }
}
