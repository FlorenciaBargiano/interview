package com.interview.bci.service;

import com.interview.bci.configuration.TokenManager;
import com.interview.bci.entity.BadRequestException;
import com.interview.bci.entity.NotFoundException;
import com.interview.bci.entity.Phone;
import com.interview.bci.entity.User;
import com.interview.bci.entity.UserResponse;
import com.interview.bci.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private static final String email = "bargianoflorencia@otlook.com";
    private static final String password = "Flor23aaaa";
    private static final String encryptedPassword= "$2a$10$tlzO7HZgrQT.1xFbFlD6V.vEx.YDKEyTVjfOHNUz3UEKpGXxDhrN.";
    private static final String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NjA4Njk3OTQsImlhdCI6MTY2MDgzMzc5NCwianRpIjoi" +
            "N2YwZmY5MzgtOTM2Ny00M2E1LTk1NGMtZWU0MTA1ZDZjZTJlIn0.NYzTDUImXjA4Hqte4Cuo_CtpKNeTJ9xZQhBFa66K1Uufl5bkU" +
            "u9NgLG4U8Yr2vbfq9XI3NSSERkrAw24oYFWvw";
    private static final String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void createUserOnlyRequiredFieldsShouldSuccess() {
        User request = buildUserWithRequiredFields();
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(request)).thenReturn(request);
        when(tokenManager.generateJwtToken(request)).thenReturn(token);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encryptedPassword);

        UserResponse response = userService.signUp(request);

        assertAll(
                () -> assertEquals(request.getEmail(), response.getEmail()),
                () -> assertEquals(request.getPassword(), response.getPassword()),
                () -> assertEquals(response.getEmail(), email),
                () -> assertTrue(response.isActive()),
                () -> assertNotNull(response.getCreated()),
                () -> assertNull(response.getLastLogin()),
                () -> assertNotNull(response.getToken())
        );

        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).save(any());
        verify(tokenManager, times(1)).generateJwtToken(any());
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void createUserWithAllFieldsShouldSuccess() {
        User request = buildUserWithAllFields();
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(request)).thenReturn(request);
        when(tokenManager.generateJwtToken(request)).thenReturn(token);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encryptedPassword);

        UserResponse response = userService.signUp(request);

        assertAll(
                () -> assertEquals(request.getEmail(), response.getEmail()),
                () -> assertEquals(request.getPassword(), response.getPassword()),
                () -> assertEquals(response.getEmail(), email),
                () -> assertTrue(response.isActive()),
                () -> assertEquals(request.getPhones(), response.getPhones()),
                () -> assertEquals(request.getName(), response.getName()),
                () -> assertNotNull(response.getCreated()),
                () -> assertNull(response.getLastLogin()),
                () -> assertNotNull(response.getToken())
        );

        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).save(any());
        verify(tokenManager, times(1)).generateJwtToken(any());
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void createUserWithThatAlreadyExistsShouldFail() {
        User request = buildUserWithAllFields();
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.signUp(request),
                "Not valid - A user with that mail already exists");

        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, never()).save(any());
        verify(tokenManager, never()).generateJwtToken(any());
    }

    @Nested
    class TestPassword {
        @Test
        @DisplayName("MoreThanTwoDigits")
        void createUserWithPasswordWithMoreThanTwoDigitsShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("123Florencia");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");

            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("MoreThanOneUpperCase")
        void createUserWithPasswordWithMoreThanOneUpperCaseShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("12FFFlorencia");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("LessThanEightCharacters")
        void createUserWithPasswordWithLessThanEightCharactersShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("12Flore");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }


        @Test
        @DisplayName("MoreThanTwelveCharacters")
        void createUserWithPasswordWithMoreThanTwelveCharactersShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("12Florenciaaa");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("WithoutUpperCase")
        void createUserWithPasswordWithoutUpperCaseShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("12floren");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("WithoutDigits")
        void createUserWithPasswordWithoutDigitsShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setPassword("florenciaaa");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The password is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }
    }

    @Nested
    class TestEmail {
        @Test
        @DisplayName("WithoutAtSign")
        void createUserWithEmailWithoutAtSignShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setEmail("bargianoflorenciaoutlook.com");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The email provided is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("WithoutLastPartOfTheEmail")
        void createUserWithEmailWithoutLastPartOfTheEmailShouldFail() {
            User request = buildUserWithRequiredFields();
            request.setEmail("bargianoflorencia@outlook");

            assertThrows(BadRequestException.class, () -> userService.signUp(request),
                    "Not valid - The email provided is not valid");
            verify(userRepository, never()).save(any());
            verify(userRepository, never()).existsByEmail(any());
            verify(tokenManager, never()).generateJwtToken(any());
        }

        @Test
        @DisplayName("WithUnderscore")
        void createUserWithEmailWithUnderscoreShouldSuccess() {
            String email = "bargiano_florencia@outlook.com";
            User request = buildUserWithRequiredFields();
            request.setEmail(email);
            when(userRepository.existsByEmail(email)).thenReturn(false);
            when(userRepository.save(request)).thenReturn(request);
            when(tokenManager.generateJwtToken(request)).thenReturn(token);

            UserResponse response = userService.signUp(request);
            assertEquals(request.getEmail(), response.getEmail());
            assertEquals(request.getEmail(), email);

            verify(userRepository, times(1)).existsByEmail(any());
            verify(userRepository, times(1)).save(any());
            verify(tokenManager, times(1)).generateJwtToken(any());
        }

        @Test
        @DisplayName("WithDot")
        void createUserWithEmailWitDotShouldSuccess() {
            String email = "bargiano.florencia@outlook.com";
            User request = buildUserWithRequiredFields();
            request.setEmail(email);
            when(userRepository.existsByEmail(email)).thenReturn(false);
            when(userRepository.save(request)).thenReturn(request);
            when(tokenManager.generateJwtToken(request)).thenReturn(token);

            UserResponse response = userService.signUp(request);
            assertEquals(request.getEmail(), response.getEmail());
            assertEquals(request.getEmail(), email);

            verify(userRepository, times(1)).existsByEmail(any());
            verify(userRepository, times(1)).save(any());
            verify(tokenManager, times(1)).generateJwtToken(any());
        }
    }

    @Test
    void logInUserShouldSuccess() {
        User user = buildUserWithAllFieldsForLogin();
        when(tokenManager.geIdFromToken(token)).thenReturn(uuid);
        when(tokenManager.generateJwtToken(user)).thenReturn(token);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserResponse response = userService.login(token);

        assertAll(
                () -> assertNotNull(response.getEmail()),
                () -> assertNotNull(response.getPassword()),
                () -> assertTrue(response.isActive()),
                () -> assertNotNull(response.getCreated()),
                () -> assertNotNull(response.getLastLogin()),
                () -> assertNotNull(response.getToken()),
                () -> assertNotNull(response.getId())
        );

        verify(tokenManager, times(1)).geIdFromToken(any());
        verify(tokenManager, times(1)).generateJwtToken(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void logInUserNotUserPresentShouldFail() {
        when(tokenManager.geIdFromToken(token)).thenReturn(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.login(token),
                "Not Found - The user was not found by the given token");

        verify(tokenManager, times(1)).geIdFromToken(any());
        verify(tokenManager, never()).generateJwtToken(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void logInUserWhenUserIsNotActiveShouldFail() {
        User user = buildUserWithAllFieldsForLogin();
        user.setActive(false);

        when(tokenManager.geIdFromToken(token)).thenReturn(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.login(token),
                "Not valid - The user is not active");

        verify(tokenManager, times(1)).geIdFromToken(any());
        verify(tokenManager, never()).generateJwtToken(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, never()).save(any());
    }

    private User buildUserWithRequiredFields() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    private User buildUserWithAllFields() {
        return User.builder()
                .email(email)
                .password(password)
                .phones(Collections.singletonList(Phone
                        .builder()
                        .cityCode(7)
                        .countryCode("25")
                        .number(87650009)
                        .build()))
                .name("Florencia Bargiano")
                .build();
    }

    private User buildUserWithAllFieldsForLogin() {
        User user = buildUserWithAllFields();
        user.setId(uuid);
        user.setActive(true);
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        return user;
    }
}
