package com.musicservice.user.controller;

import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.security.util.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public ProfileController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<SongGetDto>> getFavouriteSongsByUserId(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<SongGetDto> song = userService.getFavouriteSongsByUserId(userDetails.getUser().getId(), page, size);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserGetDto> updateProfile(@AuthenticationPrincipal UserPrincipal userDetails, @Valid @RequestBody UserPostDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        UserGetDto user = userService.updateUser(userDto, userDetails.getUser().getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(@AuthenticationPrincipal UserPrincipal userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return new ResponseEntity<>(userDetails.getUser().getId(), HttpStatus.OK);
    }

    @PostMapping("/favorites/{songId}")
    public ResponseEntity<?> addFavouriteSong(@AuthenticationPrincipal UserPrincipal userDetails, @PathVariable int songId) {
        return new ResponseEntity<>(userService.addSongToFavourites(userDetails.getUser().getId(), songId), HttpStatus.OK);
    }

    @DeleteMapping("/favourites/{songId}")
    public ResponseEntity<?> deleteFavouriteSong(@AuthenticationPrincipal UserPrincipal userDetails, @PathVariable int songId) {
        return new ResponseEntity<>(userService.deleteSongFromFavourites(userDetails.getUser().getId(), songId), HttpStatus.OK);
    }
}
