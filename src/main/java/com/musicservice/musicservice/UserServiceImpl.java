package com.musicservice.musicservice;

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

    @Autowired
    public UserServiceImpl(MapperService mapperService) {
        this.mapperService = mapperService;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(3);
        user.setName("Jack");
        users.add(user);
        var usersdto = mapperService.usersToUserDtos(users);
        return usersdto;
    }
}
