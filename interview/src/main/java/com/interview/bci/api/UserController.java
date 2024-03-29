package com.interview.bci.api;

import com.interview.bci.entity.User;
import com.interview.bci.entity.UserResponse;
import com.interview.bci.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/sign-up",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<UserResponse> signUp(@Valid @RequestBody User user,
                                        Errors errors) {

        return new ResponseEntity<>(userService.signUp(user, errors), HttpStatus.CREATED);
    }

    @GetMapping(value = "/login",
            produces = "application/json")
    ResponseEntity<UserResponse> login(@RequestHeader("token") String token) {
        return ResponseEntity.ok(userService.login((token)));
    }

}
