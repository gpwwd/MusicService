package com.musicservice.service.mapper;

import com.musicservice.dto.get.UserGetDto;
import com.musicservice.dto.get.UserWithSongsGetDto;
import com.musicservice.dto.post.UserPostDto;
import com.musicservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperService {
    User userGetDtoToUser(UserGetDto source);
    UserGetDto userToUserGetDto(User destination);

    List<UserGetDto> usersToUserDtos(List<User> users);
    List<User> userDtosToUsers(List<UserGetDto> userDtos);

    UserWithSongsGetDto userToUserDtoWithSongs(User destination);

    User userPostDtoToUser(UserPostDto source);
    UserPostDto userToUserPostDto(User destination);
}
