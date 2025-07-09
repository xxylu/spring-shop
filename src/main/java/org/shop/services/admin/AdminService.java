package org.shop.services.admin;


import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.order.IOrderRepository;
import org.shop.repositories.order.OrderRepository;
import org.shop.repositories.user.IUserRepository;
import org.shop.repositories.user.UserRepository;

import java.util.Optional;

public class AdminService implements IAdminService {
    IUserRepository userRepository = new UserRepository();
    IOrderRepository orderRepository = new OrderRepository();


    @Override
    public void BanUser(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.BANNED);

            userRepository.updateUser(user);
        }
    }

    @Override
    public void UnBanUser(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.USER);

            // Zapisz zmiany
            userRepository.updateUser(user);
        }
    }

    @Override
    public void updateUser(String id, String status) {

    }

    @Override
    public void ChangeOrderStatus(String id, String status) {

    }
}
