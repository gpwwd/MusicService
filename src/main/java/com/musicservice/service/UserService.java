package com.musicservice.service;

import com.musicservice.dto.UserDto;
import com.musicservice.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto addUser(UserDto userDto);
}
