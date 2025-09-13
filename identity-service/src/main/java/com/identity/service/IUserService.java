package com.identity.service;

import com.identity.dto.request.PasswordCreateReq;
import com.identity.dto.request.UserReq;
import com.identity.dto.response.UserResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserReq userReq);

    UserResponse updateUser(String userId, UserReq userReq);

    void deleteUser(String id);

    @PreAuthorize("hasRole('ADMIN')")
    List<UserResponse> getAllUsers(PageRequest pageRequest);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse getUserById(String id);

    UserResponse getMyInfo();

    void createPassword(PasswordCreateReq passwordCreateReq);
}
