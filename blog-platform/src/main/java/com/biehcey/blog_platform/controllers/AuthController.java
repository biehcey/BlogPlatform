package com.biehcey.blog_platform.controllers;

import com.biehcey.blog_platform.domain.dtos.AuthResponse;
import com.biehcey.blog_platform.domain.dtos.LoginRequest;
import com.biehcey.blog_platform.domain.dtos.RegisterRequest;
import com.biehcey.blog_platform.domain.dtos.RegisterResponse;
import com.biehcey.blog_platform.domain.entities.User;
import com.biehcey.blog_platform.mappers.UserMapper;
import com.biehcey.blog_platform.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){
        User user = userMapper.registerRequestToUser(request);
        User saved = authenticationService.registerUser(user);
        RegisterResponse response = userMapper.userToRegisterResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
