package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.UserCreationRequest;
import com.caophu2305.popfigure.dto.response.UserResponse;
import com.caophu2305.popfigure.entity.Role;
import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.entity.UserRole;
import com.caophu2305.popfigure.exception.AppException;
import com.caophu2305.popfigure.exception.ErrorCode;
import com.caophu2305.popfigure.mapper.UserMapper;
import com.caophu2305.popfigure.repository.RoleRepository;
import com.caophu2305.popfigure.repository.UserRepository;
import com.caophu2305.popfigure.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.findByUsername(userCreationRequest.getName()).isPresent()
                || userRepository.findByEmail(userCreationRequest.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userCreationRequest);
        user.setUsername(userCreationRequest.getName());
        user.setHash_password(passwordEncoder.encode(user.getHash_password()));
        userRepository.save(user);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        UserRole relation = new UserRole();
        relation.setUser(user);
        relation.setRole(userRole);
        userRoleRepository.save(relation);

        return userMapper.toUserResponse(user);
    }

    public java.util.List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getMyInfo() {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new com.caophu2305.popfigure.exception.AppException(
                        com.caophu2305.popfigure.exception.ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
}
