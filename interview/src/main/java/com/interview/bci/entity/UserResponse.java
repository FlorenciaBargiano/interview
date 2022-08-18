package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String token;

    private String id;

    private String created;

    private String lastLogin;

    private boolean isActive;

    private String name;

    private String email;

    private String password;

    private List<Phone> phones = new ArrayList<>();

    public UserResponse buildUserResponse(String token, User user){
        this.token = token;
        this.id = user.getId();
        this.created = returnDateInCorrectFormat(user.getCreated());
        this.lastLogin = returnDateInCorrectFormat(user.getLastLogin());
        this.isActive = user.isActive();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phones = user.getPhones();
        return this;
    }

    private String returnDateInCorrectFormat(LocalDateTime dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh.mm.ss aa", Locale.US);
        return dateFormat.format(Timestamp.valueOf(dateTime));
    }
}
