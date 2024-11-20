package com.musicservice.service;

import com.musicservice.dto.UserDto;
import com.musicservice.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(int id);
    UserDto addUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(int id);
}
