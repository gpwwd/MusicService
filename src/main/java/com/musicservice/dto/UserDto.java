package com.musicservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Setter @Getter
@RedisHash
public class UserDto implements Serializable {

    private int id;

    private String name;

    private String email;
}
