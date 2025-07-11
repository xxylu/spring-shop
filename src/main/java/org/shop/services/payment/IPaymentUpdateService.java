package org.shop.services.payment;

import org.shop.models.Payment.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentUpdateService {
    void createPayment(Payment payment);
    void updatePayment(Payment payment);
    void deletePayment(String paymentId);
    Optional<Payment> getPaymentById(String paymentId);
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByUserId(String userId);
}
