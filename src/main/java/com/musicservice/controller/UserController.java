package com.musicservice.controller;

import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.get.UserGetDto;
import com.musicservice.dto.post.UserPostDto;
import com.musicservice.exception.CustomValidationException;
import com.musicservice.musicservice.UserServiceImpl;
import com.musicservice.service.UserService;
import com.musicservice.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserServiceImpl userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


    @GetMapping()
    public ResponseEntity<?> getUsers() {
        List<UserGetDto> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        UserGetDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("{userId}/favourite-songs")
    public ResponseEntity<?> getFavouriteSongsByUserId(@PathVariable int userId) {
        List<SongGetDto> song = userService.getFavouriteSongsByUserId(userId);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserPostDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        UserGetDto user = userService.register(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserPostDto userDto) {
        String status = userService.login(userDto);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable int id, @Valid @RequestBody UserPostDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        UserGetDto user = userService.updateUser(userDto, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/{userId}/favourite-songs/{songId}")
    public ResponseEntity<?> addFavouriteSong(@PathVariable int userId, @PathVariable int songId) {
        userService.addFavouriteSong(userId, songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
