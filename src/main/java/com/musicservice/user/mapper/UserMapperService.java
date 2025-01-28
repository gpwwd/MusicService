package com.musicservice.user.mapper;

import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.get.UserWithSongsGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.domain.model.User;
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
