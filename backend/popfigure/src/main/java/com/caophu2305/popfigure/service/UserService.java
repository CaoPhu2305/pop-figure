package com.caophu2305.popfigure.service;

import com.caophu2305.popfigure.dto.request.UserCreationRequest;
import com.caophu2305.popfigure.dto.response.UserResponse;
import com.caophu2305.popfigure.entity.User;
import com.caophu2305.popfigure.mapper.UserMapper;
import com.caophu2305.popfigure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        User user = userMapper.toUser(userCreationRequest);
        user.setHash_password(passwordEncoder.encode(user.getHash_password()));
        userRepository.save(user);
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
