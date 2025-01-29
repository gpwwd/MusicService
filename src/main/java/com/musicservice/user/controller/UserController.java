package com.musicservice.user.controller;

import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.exception.CustomValidationException;
import com.musicservice.user.service.UserService;
import com.musicservice.user.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<UserGetDto>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<UserGetDto> users = userService.getAll(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        UserGetDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<Page<SongGetDto>> getFavouriteSongsByUserId(
            @PathVariable int id,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<SongGetDto> song = userService.getFavouriteSongsByUserId(id, page, size);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
