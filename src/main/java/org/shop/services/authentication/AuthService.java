package org.shop.services.authentication;

import org.shop.models.user.Role;
import org.shop.repositories.admin.AdminRepository;
import org.shop.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private UserRepository userRepository;
    private AdminRepository adminRepository;

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean register(String username, String password, Role role) {
        return false;
    }
}
