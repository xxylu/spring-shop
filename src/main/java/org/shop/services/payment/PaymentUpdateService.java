package org.shop.services.payment;

import org.shop.models.Payment.Payment;
import org.shop.repositories.payment.PaymentRepository;
import org.shop.services.order.IOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentUpdateService implements IPaymentUpdateService {
    private final PaymentRepository paymentRepository;

    public PaymentUpdateService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void createPayment(Payment payment) {
        paymentRepository.create(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        paymentRepository.modify(payment);
    }

    @Override
    public void deletePayment(String paymentId) {
        paymentRepository.delete(paymentId);
    }

    @Override
    public Optional<Payment> getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }
}
