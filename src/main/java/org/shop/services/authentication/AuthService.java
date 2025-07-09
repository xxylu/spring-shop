package org.shop.services.authentication;

import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.admin.AdminRepository;
import org.shop.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository = new UserRepository();
    private final AdminRepository adminRepository = new AdminRepository();

    @Override
    public Boolean isUserActive(String id) {
        return userRepository.findById(id)
                .map(User::getIsActive)
                .orElse(null);
    }

    @Override
    public Boolean userExists(String login) {
        Optional<User> user =  userRepository.findByUsername(login);
        return user.isPresent();
    }

    @Override
    public Boolean checkUserCredentials(String login, String password) {
        Optional<User> user =  userRepository.findByUsername(login);
        return user.isPresent() && user.get().getPassword().equals(password) && user.get().getLogin().equals(login);
    }

    @Override
    public Optional<User> login(String login, String password) {
        if(!userExists(login)) {
            System.out.println("User nie isnteje");
            return Optional.empty();
        }

        if(checkUserCredentials(login, password)) {
            Optional<User> user =  userRepository.findByUsername(login);
            System.out.println("Zalogowano pomyślnie");
            return user;
        }
        System.out.println("Błędny login lub hasło");
        return Optional.empty();
    }

    @Override
    public boolean register(String username, String password, Role role) {
        if(!userExists(username)) {
            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(username)
                    .password(password)
                    .role(role)
                    .isActive(false)
                    .build();

            userRepository.addUser(user);
            System.out.println("Dodano nowego usera");
            return true;
        }
        System.out.println("User już istnieje");
        return false;
    }
}
