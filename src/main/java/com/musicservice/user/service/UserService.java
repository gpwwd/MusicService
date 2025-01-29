package com.musicservice.user.service;

import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.exception.SongAlreadyFavouriteException;
import com.musicservice.exception.SongNotFoundException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.domain.model.Song;
import com.musicservice.domain.model.User;
import com.musicservice.domain.repository.jpa.SongRepository;
import com.musicservice.domain.repository.jpa.UserRepository;
import com.musicservice.catalog.mapper.SongMapperService;
import com.musicservice.user.mapper.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final int ENCODER_STRENGTH = 12;

    private final UserRepository userRepository;
    private  final SongRepository songRepository;
    private final UserMapperService userMapperService;
    private final SongMapperService songMapperService;

    @Autowired
    public UserService(@Qualifier("jpaUserRepository") UserRepository userRepository, UserMapperService userMapperService,
                       SongRepository songRepository, SongMapperService songMapperService) {
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
        this.songRepository = songRepository;
        this.songMapperService = songMapperService;
    }

    public Page<UserGetDto> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageRequest);
        return users.map(userMapperService::userToUserGetDto);
    }

    public UserGetDto getById(int id) {
        Optional<User> foundUser = userRepository.findById(id);

        User user;
        if(foundUser.isPresent()) {
            user = foundUser.get();
        } else {
            throw new SongNotFoundException(id);
        }
        return userMapperService.userToUserGetDto(user);
    }

    public Page<SongGetDto> getFavouriteSongsByUserId(int userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Song> favouriteSongs = songRepository.findByUsersId(userId, pageRequest);
        return favouriteSongs.map(songMapperService::songToSongGetDto);
    }

    @Transactional
    public UserGetDto updateUser(UserPostDto userDto, int id) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        User existingUser = existingUserOpt.get();
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        User user = userRepository.save(existingUser);
        return userMapperService.userToUserGetDto(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void addFavouriteSong(int userId, int songId) {
        Optional<User> existingUserOpt = userRepository.findById(userId);
        if (existingUserOpt.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Song> existingSongOpt = songRepository.findById(songId);
        if (existingSongOpt.isEmpty()) {
            throw new SongNotFoundException(songId);
        }

        User user = existingUserOpt.get();
        Song song = existingSongOpt.get();

        if (user.getFavouriteSongs().contains(song)) {
            throw new SongAlreadyFavouriteException(songId);
        }

        user.getFavouriteSongs().add(song);
        song.getUsers().add(user);

        userRepository.save(user);
        songRepository.save(song);
    }
}
