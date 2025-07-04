package org.example.repositories.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.database.DatabaseConnection;
import org.example.models.user.User;

public class UserRepository {

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .isActive(rs.getBoolean("isActive"))
                        .build();
                users.add(user);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException("Error occuerd while reading database", e);
        }
        return users;
    }
}
