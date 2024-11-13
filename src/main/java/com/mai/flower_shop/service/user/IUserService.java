package com.mai.flower_shop.service.user;

import com.mai.flower_shop.dto.UserDto;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.request.CreateUserRequest;
import com.mai.flower_shop.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
