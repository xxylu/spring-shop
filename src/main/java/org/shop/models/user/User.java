package org.shop.models.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userid;
    private String login;
    private String passwd;
    private Role roles;
    private Boolean isActive;
}
