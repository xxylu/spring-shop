package org.shop.repositories.user;

import org.shop.models.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    Optional<User> findById(String id);
    User findByUsername(String username);
    Boolean isUserActive(String id);
    boolean userExists(String login);
    void addUser(User user);
}
