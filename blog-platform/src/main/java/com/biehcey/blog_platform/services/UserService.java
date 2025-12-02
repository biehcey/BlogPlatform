package com.biehcey.blog_platform.services;

import com.biehcey.blog_platform.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
