package com.musicservice.musicservice;

import com.musicservice.dao.SongDao;
import com.musicservice.dao.UserDao;
import com.musicservice.dto.UserDto;
import com.musicservice.exception.SongNotFoundException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.User;
import com.musicservice.service.MapperService;
import com.musicservice.service.UserMapperService;
import com.musicservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final MapperService mapperService;
    private final UserMapperService userMapperService;
    private final UserDao userDao;
    private final SongDao songDao;

    @Autowired
    public UserServiceImpl(MapperService mapperService, UserDao userDao, SongDao songDao,
        UserMapperService userMapperService) {
        this.mapperService = mapperService;
        this.userDao = userDao;
        this.songDao = songDao;
        this.userMapperService = userMapperService;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userDao.getUsers();
        return userMapperService.usersToUserDtos(users);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(int id) {
        User user = userDao.getUserById(id);
        return userMapperService.userToUserDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapperService.userDtoToUser(userDto);
        userDao.addUser(user);
        return userDto;
    }

    @Override
    @CacheEvict(value = "users", key = "#userDto.id")
    public UserDto updateUser(UserDto userDto) {
        User user = userMapperService.userDtoToUser(userDto);
        userDao.updateUser(user.getId(), user);
        return userDto;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public UserDto updateUser(int id, UserDto userDto) {
        User user = userMapperService.userDtoToUser(userDto);
        userDao.updateUser(id, user);
        return userDto;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public void addFavouriteSong(int userId, int songId) {
        //обработать ситуацию, когда песня у пользователя уже существует
        if (!userDao.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!songDao.songExists(songId)) {
            throw new SongNotFoundException(songId);
        }
        userDao.addFavouriteSong(userId, songId);
    }
}