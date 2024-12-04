package com.musicservice.service;

import com.musicservice.dto.UserDto;
import com.musicservice.dto.UserDtoWithSongs;
import com.musicservice.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(int id);
    UserDto addUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    UserDto updateUser(int id, UserDto userDto);
    void deleteUser(int id);
    UserDtoWithSongs getUserWithFavouriteSong(int userId);
    void addFavouriteSong(int userId, int songId);
}
