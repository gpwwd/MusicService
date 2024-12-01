package com.musicservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Setter @Getter
@RedisHash("User")
public class User implements Serializable {
    @Id
    private int id;
    private String name;
    private String email;
    private List<Song> favouriteSongs;
}
