package com.biehcey.blog_platform.mappers;

import com.biehcey.blog_platform.domain.dtos.RegisterRequest;
import com.biehcey.blog_platform.domain.dtos.RegisterResponse;
import com.biehcey.blog_platform.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User registerRequestToUser(RegisterRequest request);
    RegisterResponse userToRegisterResponse(User user);
}
