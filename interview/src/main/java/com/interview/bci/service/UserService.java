package com.interview.bci.service;

import com.interview.bci.configuration.TokenManager;
import com.interview.bci.entity.ErrorResponse;
import com.interview.bci.entity.User;
import com.interview.bci.entity.UserResponse;
import com.interview.bci.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;

    public UserResponse signUp(final User user) throws ErrorResponse {

        validateFields(user);
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        if (userRepository.existsByEmail(user.getEmail()))
            generateException(400, "Bad Request - A user with that mail already exists");
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        user.setCreated(LocalDateTime.now());
        userRepository.save(user);
        return UserResponse.builder()
                .user(user)
                .token(tokenManager.generateJwtToken(user))
                .build();
    }

    public UserResponse login(final String token) throws ErrorResponse {
        String userId = tokenManager.geIdFromToken(token);

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
            generateException(404, "Not Found - The user was not found by the given token");

        return UserResponse.builder()
                .user(user.get())
                .token(tokenManager.generateJwtToken(user.get()))
                .build();
    }

    private void validateEmail(String email) throws ErrorResponse {

        Pattern pattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = pattern.matcher(email);
        if (!regMatcher.matches()) {
            generateException(400, "Bad Request - The email is not valid");
        }
    }

    private void validatePassword(String password) throws ErrorResponse {

        int countCapital = 0;
        int countDigit = 0;

        Pattern pattern = Pattern.compile("[a-zA-Z-0-9]{8,12}$");
        Matcher regMatcher = pattern.matcher(password);
        if (!regMatcher.matches()) {
            generateException(400, "Bad Request - The password is not valid");
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (countCapital == 1) {
                    generateException(400, "Bad Request - The password is not valid. The number of upper cases " +
                            "are greater than 2");
                } else {
                    countCapital++;
                }
            } else if (Character.isDigit(ch)) {
                if (countDigit == 1) {
                    generateException(400, "Bad Request - The password is not valid. The number of digits are " +
                            "greater than 2");
                } else {
                    countDigit++;
                }
            }
        }
    }

    private void validateFields(User user) throws ErrorResponse {
        Optional<String> email = Optional.ofNullable(user.getName());
        Optional<String> password = Optional.ofNullable(user.getPassword());

        if (!email.isPresent() || !password.isPresent())
            generateException(400, "Bad Request - Email and password are not nullable fields");
    }

    private void generateException(int code, String detail) throws ErrorResponse {
        throw new ErrorResponse(LocalDateTime.now(), code, detail);
    }
}
