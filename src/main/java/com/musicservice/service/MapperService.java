package com.musicservice.service;

import com.musicservice.dto.UserDto;
import com.musicservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperService {
    User sourceToDestination(UserDto source);
    UserDto destinationToSource(User destination);

    List<UserDto> usersToUserDtos(List<User> users);
    List<User> userDtosToUsers(List<UserDto> userDtos);
}
