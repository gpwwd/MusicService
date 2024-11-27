package com.musicservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@RedisHash
public class Comment implements Serializable {
    private Integer id;
    private String comment;
    private int rating;
}
