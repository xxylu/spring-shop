package org.shop.repositories.user;

import org.shop.models.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User>  findByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(String id);
}
