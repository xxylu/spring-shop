package org.shop.services.authentication;

import org.shop.models.user.Role;

public interface IAuthService {
    boolean login(String username, String password);
    boolean register(String username, String password, Role role);

}
