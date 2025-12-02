package com.biehcey.blog_platform.services;

import com.biehcey.blog_platform.domain.dtos.RegisterRequest;
import com.biehcey.blog_platform.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
    User registerUser(User user);
}
