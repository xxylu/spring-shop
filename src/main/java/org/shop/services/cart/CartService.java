package org.shop.services.cart;


import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.order.IOrderRepository;
import org.shop.repositories.order.OrderRepository;
import org.shop.repositories.user.IUserRepository;
import org.shop.repositories.user.UserRepository;

import java.util.Optional;

public class AdminService implements ICartService {
    IUserRepository userRepository = new UserRepository();
    IOrderRepository orderRepository = new OrderRepository();
}
