package com.identity.service.impl;

import com.identity.client.ProfileClient;
import com.identity.dto.request.PasswordCreateReq;
import com.identity.dto.request.ProfileReq;
import com.identity.dto.request.UserReq;
import com.identity.dto.response.UserResponse;
import com.identity.entity.Role;
import com.identity.entity.User;
import com.identity.exception.ErrorCode;
import com.identity.exception.ServiceException;
import com.identity.mapper.ProfileMapper;
import com.identity.mapper.UserMapper;
import com.identity.repo.RoleRepo;
import com.identity.repo.UserRepo;
import com.identity.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

import static com.identity.constant.Constants.PreDefineRole.ROLE_USER;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService implements IUserService {
    UserRepo userRepo;
    UserMapper userMapper;
    RoleRepo roleRepo;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @Override
    public UserResponse createUser(UserReq userReq) {
        if (userRepo.findByUsernameAndActiveTrue(userReq.getUsername()).isPresent())
            throw new ServiceException(ErrorCode.USER_3001);

        User user = userMapper.toUserFromUserReq(userReq);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(userReq.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepo.findById(ROLE_USER).ifPresent(roles::add);
        user.setRoles(roles);

        User savedUser = userRepo.save(user);
        ProfileReq profileReq = profileMapper.toProfileReq(userReq);
        profileReq.setUserId(savedUser.getId());
//        profileClient.createProfile(profileReq);

        return userMapper.toUserResFromUser(savedUser);
    }

    @Override
    public UserResponse updateUser(String userId, UserReq userReq) {
        User user = userRepo.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_3002));

        userMapper.updateUser(user, userReq);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResFromUser(userRepo.save(user));
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_3002));
        user.setActive(false);
        userRepo.save(user);
    }

    @Override
    public List<UserResponse> getAllUsers(PageRequest pageRequest) {
        return userRepo.findAllByActiveTrue(pageRequest).stream()
                .map(userMapper::toUserResFromUser).toList();
    }

    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResFromUser(
                userRepo.findByIdAndActiveTrue(id).orElseThrow(() -> new ServiceException(ErrorCode.USER_3002))
        );
    }

    @Override
    public UserResponse getMyInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepo.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_3002));
        var userResponse = userMapper.toUserResFromUser(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }

    @Override
    public void createPassword(PasswordCreateReq req) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        var user = userRepo.findByUsernameAndActiveTrue(username).orElseThrow(
                () -> new ServiceException(ErrorCode.USER_3002)
        );
        if (StringUtils.hasText(user.getPassword())) throw new ServiceException(ErrorCode.USER_3003);

        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepo.save(user);
    }
}
