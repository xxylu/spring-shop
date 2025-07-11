package org.shop.repositories.product;

import org.shop.database.DatabaseConnection;
import org.shop.models.product.Product;
import org.shop.models.product.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductRepository implements IProductRepository {

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (productid, price, title, description, category, isactive) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getTitle());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getCategory().name());
            statement.setBoolean(6, Boolean.TRUE.equals(product.getIsactive())); // zabezpieczenie przed null

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving product", e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "UPDATE products SET isactive = FALSE WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deactivating product", e);
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Product product = mapResultSetToProduct(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while fetching all products", e);
        }
        return products;
    }

    @Override
    public Product findByName(String name) {
        String sql = "SELECT * FROM products WHERE title = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduct(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding product by name", e);
        }
        return null;
    }

    @Override
    public Product findById(String id) {
        String sql = "SELECT * FROM products WHERE productid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduct(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding product by id", e);
        }
        return null;
    }

    private static Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getString("productid"))
                .price(rs.getDouble("price"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .category(ProductCategory.valueOf(rs.getString("category")))
                .isactive(rs.getBoolean("isactive"))
                .build();
    }
}
