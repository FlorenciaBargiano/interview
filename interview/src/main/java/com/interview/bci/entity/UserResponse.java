package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String token;

    private String id;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private boolean isActive;

    private String name;

    private String email;

    private String password;

    private List<Phone> phones = new ArrayList<>();

    public UserResponse buildUserResponse(String token, User user){
        this.token = token;
        this.id = user.getId();
        this.created = user.getCreated();
        this.lastLogin = user.getLastLogin();
        this.isActive = user.isActive();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phones = user.getPhones();
        return this;
    }
}
