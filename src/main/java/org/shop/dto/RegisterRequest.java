package org.shop.dto;

public class RegisterRequest {
    String login;
    String password;
    String role;

    public String getLogin() {return login;}

    public void setLogin(String login) {this.login = login;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
