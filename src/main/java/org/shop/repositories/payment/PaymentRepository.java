package org.shop.repositories.payment;

import org.shop.database.DatabaseConnection;
import org.shop.models.Payment.Payment;
import org.shop.models.Payment.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository implements IPaymentRepository {

    @Override
    public void create(Payment payment) {
        String sql = "INSERT INTO payment (paymentid, orderid, userid, paymentstatus) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, payment.getPaymentid());
            stmt.setString(2, payment.getOrderid());
            stmt.setString(3, payment.getUserid());
            stmt.setString(4, payment.getPaymentStatus().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modify(Payment payment) {
        String sql = "UPDATE payment SET orderid = ?, userid = ?, paymentstatus = ? WHERE paymentid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, payment.getOrderid());
            stmt.setString(2, payment.getUserid());
            stmt.setString(3, payment.getPaymentStatus().name());
            stmt.setString(4, payment.getPaymentid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String paymentId) {
        String sql = "DELETE FROM payment WHERE paymentid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Payment> findById(String paymentId) {
        String sql = "SELECT * FROM payment WHERE paymentid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                Statement stmt = connection.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                payments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public List<Payment> findByUserId(String userId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE userid = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return Payment.builder()
                .paymentid(rs.getString("paymentid"))
                .orderid(rs.getString("orderid"))
                .userid(rs.getString("userid"))
                .paymentStatus(PaymentStatus.valueOf(rs.getString("paymentstatus")))
                .build();
    }
}
