package com.musicservice.musicservice;

import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.get.UserGetDto;
import com.musicservice.dto.post.UserPostDto;
import com.musicservice.exception.SongAlreadyFavouriteException;
import com.musicservice.exception.SongNotFoundException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import com.musicservice.repository.jpa.SongRepository;
import com.musicservice.repository.jpa.UserRepository;
import com.musicservice.service.mapper.MapperService;
import com.musicservice.service.mapper.UserMapperService;
import com.musicservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private  final SongRepository songRepository;
    private final UserMapperService userMapperService;
    private final MapperService songMapperService;

    @Autowired
    public UserServiceImpl(@Qualifier("jpaUserRepository") UserRepository userRepository, UserMapperService userMapperService,
                           SongRepository songRepository, MapperService songMapperService) {
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
        this.songRepository = songRepository;
        this.songMapperService = songMapperService;
    }

    @Override
    public List<UserGetDto> getAll() {
        List<User> songs = userRepository.findAll();
        return userMapperService.usersToUserDtos(songs);
    }

    @Override
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

    @Override
    public List<SongGetDto> getFavouriteSongsByUserId(int userId) {
        List<Song> favouriteSongs = songRepository.findByUsersId(userId);
        List<SongGetDto> songGetDtos = songMapperService.songsToSongGetDtos(favouriteSongs);
        return songGetDtos;
    }

    @Override
    @Transactional
    public UserGetDto save(UserPostDto userDto) {
        User user = userMapperService.userPostDtoToUser(userDto);
        User saved = userRepository.save(user);
        return userMapperService.userToUserGetDto(saved);
    }

    @Override
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

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
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
