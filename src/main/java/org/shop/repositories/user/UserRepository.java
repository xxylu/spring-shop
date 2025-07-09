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
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserRepository {

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String roleStr = rs.getString("roles");
                Role role;
                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Unknown role: " + roleStr, e);
                }

                User user = User.builder()
                        .userid(rs.getString("userid"))
                        .login(rs.getString("login"))
                        .passwd(rs.getString("passwd"))
                        .roles(role)
                        .isActive(rs.getBoolean("isActive"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading database", e);
        }
        return users;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE userid = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String roleStr = rs.getString("roles");
                    Role role;
                    try {
                        role = Role.valueOf(roleStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Unknown role: " + roleStr, e);
                    }

                    User user = User.builder()
                            .userid(rs.getString("userid"))
                            .login(rs.getString("login"))
                            .passwd(rs.getString("passwd"))
                            .roles(role)
                            .isActive(rs.getBoolean("isActive"))
                            .build();
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading database", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String roleStr = rs.getString("roles");
                    Role role;
                    try {
                        role = Role.valueOf(roleStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Unknown role: " + roleStr, e);
                    }

                    User user = User.builder()
                            .userid(rs.getString("userid"))
                            .login(rs.getString("login"))
                            .passwd(rs.getString("passwd"))
                            .roles(role)
                            .isActive(rs.getBoolean("isActive"))
                            .build();
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while querying user by username", e);
        }
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (userid, login, passwd, roles, isActive) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getUserid());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getPasswd());
            stmt.setString(4, user.getRoles().name());
            stmt.setBoolean(5, user.getIsActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while adding user", e);
        }
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET login = ?, passwd = ?, roles = ?, isActive = ? WHERE userid = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPasswd());
            stmt.setString(3, user.getRoles().name());
            stmt.setBoolean(4, user.getIsActive());
            stmt.setString(5, user.getUserid());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while updating user", e);
        }
    }

    @Override
    public void deleteUser(String userid) {
        String sql = "DELETE FROM users WHERE userid = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting user", e);
        }
    }
}
