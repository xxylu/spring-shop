package org.shop.dto;

public class LoginRequest {
    private String login;
    private String password;

    // gettery i settery
    public String getLogin() { return login; }
    public void setLogin(String username) { this.login = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
