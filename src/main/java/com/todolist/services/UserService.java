package com.todolist.services;

import com.todolist.dto.UserProfileDto;
import com.todolist.entities.UserProfile;

import java.util.List;


public interface UserService {
    UserProfile createUser(UserProfileDto userDto);

    List<UserProfile> getAllUsers();

    UserProfile findUserByIdOrName(String idOrName);

    UserProfile updateUser(Long userId, UserProfileDto updatedUserDto);

    UserProfile deleteUserById(Long userId);
}
