package com.caophu2305.popfigure.mapper;

import com.caophu2305.popfigure.dto.request.UserCreationRequest;
import com.caophu2305.popfigure.dto.response.UserResponse;
import com.caophu2305.popfigure.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

}
