package com.musicservice.service;

import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.get.UserGetDto;
import com.musicservice.dto.post.UserPostDto;

import java.util.List;

public interface UserService {
    List<UserGetDto> getAll();
    UserGetDto getById(int id);
    List<SongGetDto> getFavouriteSongsByUserId(int userId);
    UserGetDto save(UserPostDto userDto);
    UserGetDto updateUser(UserPostDto userDto, int id);
    void deleteUser(int id);
    void addFavouriteSong(int userId, int songId);
}
