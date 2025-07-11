package org.shop.repositories.payment;
import org.shop.models.Payment.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository {
    void create(Payment payment);
    void modify(Payment payment);
    void delete(String paymentId);
    Optional<Payment> findById(String paymentId);
    List<Payment> findAll();
    List<Payment> findByUserId(String userId);
}
