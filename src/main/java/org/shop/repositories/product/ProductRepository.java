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


public class ProductRepository implements IProductRepository {

    @Override
    public Boolean addProduct(Product product) {
        // Sprawdzenie, czy produkt już istnieje w bazie
        if (findById(product.getId()) != null) {
            System.out.println("Taki produkt już istnieje");
            return false;
        }

        String sql = "INSERT INTO product (id, name, description, category, isactive) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getCategory().name());
            statement.setBoolean(5, Boolean.TRUE.equals(product.getIsactive())); // zabezpieczenie przed null

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving product", e);
        }
    }

    @Override
    public Boolean deleteById(String id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting product", e);
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
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
        String sql = "SELECT * FROM product WHERE name = ?";
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
        String sql = "SELECT * FROM product WHERE id = ?";
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
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .category(ProductCategory.valueOf(rs.getString("category")))
                .isactive(rs.getBoolean("isactive"))
                .build();
    }
}
