package org.shop.services.authentication;

import org.mindrot.jbcrypt.BCrypt;
import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.user.IUserRepository;
import org.shop.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository = new UserRepository();


    @Override
    public Boolean isUserActive(String id) {
        return userRepository.findById(id)
                .map(User::getIsActive)
                .orElse(false);
    }

    @Override
    public Boolean userExists(String login) {
        return userRepository.findByUsername(login).isPresent();
    }

    @Override
    public Boolean checkUserCredentials(String login, String rawPassword) {
        Optional<User> user = userRepository.findByUsername(login);
        return user.isPresent() && BCrypt.checkpw(rawPassword, user.get().getPassword());
    }

    @Override
    public Optional<User> login(String login, String password) {
        if(!userExists(login)) {
            System.out.println("User nie istnieje");
            return Optional.empty();
        }

        if(checkUserCredentials(login, password)) {
            Optional<User> user = userRepository.findByUsername(login); // już było wcześniej
            System.out.println("Zalogowano pomyślnie");
            return user;
        }
        System.out.println("Błędny login lub hasło");
        return Optional.empty();
    }

    @Override
    public boolean register(String username, String password, Role role) {
        if(!userExists(username)) {
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(username)
                    .password(hashed)
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
