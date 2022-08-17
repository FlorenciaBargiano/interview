package com.interview.bci.service;

import com.interview.bci.configuration.TokenManager;
import com.interview.bci.entity.BadRequestException;
import com.interview.bci.entity.NotFoundException;
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

    public UserResponse signUp(final User user) {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        if (userRepository.existsByEmail(user.getEmail()))
            generateBadRequestException("Not valid - A user with that mail already exists");
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        user.setCreated(LocalDateTime.now());
        User userSaved = userRepository.save(user);
        String tokenJWT = tokenManager.generateJwtToken(userSaved);
        return new UserResponse().buildUserResponse(tokenJWT, userSaved);
    }

    public UserResponse login(final String token) {

        String userId = tokenManager.geIdFromToken(token);

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
            throw new NotFoundException(LocalDateTime.now(), "Not Found - The user was not found by the given token");

        if( !user.get().isActive())
            generateBadRequestException("Not valid - The user is not active");

        user.get().setLastLogin(LocalDateTime.now());
        User userSaved = userRepository.save(user.get());
        String tokenJWT = tokenManager.generateJwtToken(userSaved);
        return new UserResponse().buildUserResponse(tokenJWT, userSaved);
    }

    private void validateEmail(String email) {
        Pattern pattern = Pattern.compile(patternEmail);
        Matcher regMatcher = pattern.matcher(email);
        if (!regMatcher.matches())
            generateBadRequestException("Not valid - The email provided is not valid");
    }

    private void validatePassword(String password) {

        List<Character> listDigits;
        List<Character> listCapitals= new ArrayList<>();

        Pattern pattern = Pattern.compile(patternPassword);
        Matcher regMatcher = pattern.matcher(password);
        if (!regMatcher.matches())
            generateBadRequestException("Not valid - The password is not valid");

       listDigits = password.chars().mapToObj((i) -> (char)i)
                .peek(character -> {
                            if (Character.isUpperCase(character))
                                listCapitals.add(character);
                        })
                 .filter(Character::isDigit)
                 .collect(Collectors.toList());

        if(listCapitals.size() !=1 || listDigits.size() != 2)
            generateBadRequestException("Not valid - The password is not valid. It should have at least " +
                    "one capital letter and two digits");
    }

    private void generateBadRequestException(String detail) {
        throw new BadRequestException(LocalDateTime.now(), detail);
    }

}
