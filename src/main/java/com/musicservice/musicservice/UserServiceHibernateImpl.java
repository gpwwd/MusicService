//package com.musicservice.musicservice;
//
//import com.musicservice.dao.SongDao;
//import com.musicservice.dao.UserDao;
//import com.musicservice.dto.get.SongGetDto;
//import com.musicservice.dto.get.UserGetDto;
//import com.musicservice.dto.post.UserPostDto;
//import com.musicservice.exception.SongNotFoundException;
//import com.musicservice.exception.UserNotFoundException;
//import com.musicservice.model.User;
//import com.musicservice.service.MapperService;
//import com.musicservice.service.UserMapperService;
//import com.musicservice.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserServiceHibernateImpl implements UserService {
//
//    private final MapperService mapperService;
//    private final UserMapperService userMapperService;
//    private final UserDao userDao;
//    private final SongDao songDao;
//
//    @Autowired
//    public UserServiceHibernateImpl(MapperService mapperService, UserDao userDao, SongDao songDao,
//                                    UserMapperService userMapperService) {
//        this.mapperService = mapperService;
//        this.userDao = userDao;
//        this.songDao = songDao;
//        this.userMapperService = userMapperService;
//    }
//
//    @Override
//    public List<UserGetDto> getAll() {
//        List<User> users = userDao.getUsers();
//        return userMapperService.usersToUserDtos(users);
//    }
//
//    @Override
//    @Cacheable(value = "users", key = "#id")
//    public UserGetDto getById(int id) {
//        User user = userDao.getUserById(id);
//        return userMapperService.userToUserGetDto(user);
//    }
//
//    @Override
//    public List<SongGetDto> getFavouriteSongsByUserId(int userId) {
//        return List.of();
//    }
//
//    @Override
//    public UserGetDto save(UserPostDto userDto) {
//        User user = userMapperService.userPostDtoToUser(userDto);
//        userDao.addUser(user);
//        return userMapperService.userToUserGetDto(user);
//    }
//
//    @Override
//    @CacheEvict(value = "users", key = "#id")
//    public UserGetDto updateUser(UserPostDto userDto, int id) {
//        User user = userMapperService.userPostDtoToUser(userDto);
//        userDao.updateUser(id, user);
//        return userMapperService.userToUserGetDto(user);
//    }
//
//    @Override
//    @CacheEvict(value = "users", key = "#id")
//    public void deleteUser(int id) {
//        userDao.deleteUser(id);
//    }
//
//    @Override
//    public void addFavouriteSong(int userId, int songId) {
//        if (!userDao.userExists(userId)) {
//            throw new UserNotFoundException(userId);
//        }
//        if (!songDao.songExists(songId)) {
//            throw new SongNotFoundException(songId);
//        }
//        userDao.addFavouriteSong(userId, songId);
//    }
//}