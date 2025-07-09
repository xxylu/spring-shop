package org.shop.services.authentication;

import org.shop.models.user.Role;
import org.shop.models.user.User;

import java.util.Optional;

public interface IAuthService {
    Boolean isUserActive(String id);
    Boolean userExists(String login);
    Boolean checkUserCredentials(String login, String password);
    Optional<User> login(String username, String password);
    boolean register(String username, String password, Role role);
    void deleteUser(String adminId, String userId);
    void activateUser(String adminId, String userId);
}
