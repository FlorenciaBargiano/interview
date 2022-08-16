package com.interview.bci.service;

import com.interview.bci.configuration.TokenManager;
import com.interview.bci.entity.GenericException;
import com.interview.bci.entity.User;
import com.interview.bci.entity.UserResponse;
import com.interview.bci.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private static final String patternEmail = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-Z)]+\\.[(a-zA-z)]{2,3}$";
    private static final String patternPassword = "[a-zA-Z-0-9]{8,12}$";

    public UserResponse signUp(final User user) throws GenericException {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        if (userRepository.existsByEmail(user.getEmail()))
            generateException(400, "Not valid - A user with that mail already exists");
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        user.setCreated(LocalDateTime.now());
        userRepository.save(user);
        return UserResponse.builder()
                .user(user)
                .token(tokenManager.generateJwtToken(user))
                .build();
    }

    public UserResponse login(final String userId) throws GenericException {

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
            generateException(404, "Not Found - The user was not found by the given token");

        user.get().setLastLogin(LocalDateTime.now());
        userRepository.save(user.get());

        return UserResponse.builder()
                .user(user.get())
                .token(tokenManager.generateJwtToken(user.get()))
                .build();
    }

    private void validateEmail(String email) throws GenericException {
        Pattern pattern = Pattern.compile(patternEmail);
        Matcher regMatcher = pattern.matcher(email);
        if (!regMatcher.matches())
            generateException(400, "Not valid - The email provided is not valid");
    }

    private void validatePassword(String password) throws GenericException {

        List<Character> listDigits;
        List<Character> listCapitals= new ArrayList<>();

        Pattern pattern = Pattern.compile(patternPassword);
        Matcher regMatcher = pattern.matcher(password);
        if (!regMatcher.matches())
            generateException(400, "Not valid - The password is not valid");

       listDigits = password.chars().mapToObj((i) -> (char)i)
                .peek(character -> {
                            if (Character.isUpperCase(character))
                                listCapitals.add(character);
                        })
                 .filter(Character::isDigit)
                 .collect(Collectors.toList());

        if(listCapitals.size() !=1 || listDigits.size() != 2)
            generateException(400, "Not valid - The password is not valid. It should have at least " +
                    "one capital letter and two digits");
    }

    private void generateException(int code, String detail) throws GenericException {
        throw new GenericException(LocalDateTime.now(), code, detail);
    }
}