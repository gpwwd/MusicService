package com.musicservice.dto.get;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Setter @Getter
@RedisHash
public class UserGetDto implements Serializable {

    private int id;

    private String name;

    private String email;
}
