package org.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {
        url = System.getenv("DB_URL");
        if (url == null) {
            throw new RuntimeException("DB_URL not set!");
        }
    }
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.print("Błąd połaczenia z Bazą Danych");
            System.exit(0);
            throw new RuntimeException(e);
        }
    }
}
