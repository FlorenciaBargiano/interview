package com.interview.bci.api;

import com.interview.bci.entity.ErrorResponse;
import com.interview.bci.entity.UserResponse;
import com.interview.bci.entity.User;
import com.interview.bci.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    ResponseEntity<UserResponse> signUp(@RequestBody User user) throws ErrorResponse {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @GetMapping("/login")
    ResponseEntity<UserResponse> login(@RequestBody String token) throws ErrorResponse {
        return  ResponseEntity.ok(userService.login(token));
    }
}
