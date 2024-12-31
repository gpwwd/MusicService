package com.musicservice.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserPostDto implements Serializable {
    private String name;

    private String email;
}
