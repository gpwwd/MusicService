package com.musicservice.security.service;

import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.domain.model.User;
import com.musicservice.domain.repository.jpa.UserRepository;
import com.musicservice.user.mapper.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private static final int ENCODER_STRENGTH = 12;

    private final UserRepository userRepository;
    private final UserMapperService userMapperService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(@Qualifier("jpaUserRepository") UserRepository userRepository,
                       UserMapperService userMapperService,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserGetDto register(UserPostDto userDto) {
        User user = userMapperService.userPostDtoToUser(userDto);
        user.setPassword(new BCryptPasswordEncoder(ENCODER_STRENGTH).encode(userDto.getPassword()));
        User saved = userRepository.save(user);
        return userMapperService.userToUserGetDto(saved);
    }

    // to return userAuthResponseDto
    public String login(UserPostDto userDto) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getName(), userDto.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(userDto.getName());
        }

        return "fail";
    }
}
