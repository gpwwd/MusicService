package com.musicservice.dto.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter @Setter
@RedisHash
public class SongRedisDto implements Serializable {
    private int id;
    private String title;
}
