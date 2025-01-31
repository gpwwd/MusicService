package com.musicservice.user.service;

import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.musicservice.exception.BadRequestException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

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
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapperService.userToUserGetDto(foundUser);
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
    public int addSongToFavourites(int userId, int songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId));

        if (user.getFavouriteSongs().contains(song))
            throw new BadRequestException(
                    "Song with id '" + songId + "' already has already been added to favourites.");

        user.getFavouriteSongs().add(song);
        song.getUsers().add(user);
        userRepository.save(user);
        songRepository.save(song);

        return song.getId();
    }

    public int deleteSongFromFavourites(int userId, int songId) throws BadRequestException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId));

        if (!user.getFavouriteSongs().contains(song)) {
            throw new BadRequestException("User with ID " + songId + " not found");
        }

        user.getFavouriteSongs().remove(song);
        song.getUsers().remove(user);
        userRepository.save(user);
        songRepository.save(song);

        return song.getId();
    }
}
