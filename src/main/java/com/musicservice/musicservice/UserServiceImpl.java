package com.musicservice.musicservice;

import com.musicservice.dao.UserDao;
import com.musicservice.dto.UserDto;
import com.musicservice.model.User;
import com.musicservice.service.MapperService;
import com.musicservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private MapperService mapperService;
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(MapperService mapperService, UserDao userDao) {
        this.mapperService = mapperService;
        this.userDao = userDao;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userDao.getUsers();
        return mapperService.usersToUserDtos(users);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = mapperService.sourceToDestination(userDto);
        userDao.addUser(user);
        return userDto;
    }
}
