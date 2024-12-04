package com.musicservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserDtoWithSongs {
    private int id;

    private String name;

    private String email;

    private List<SongDtoWithNoComments> favouriteSongs;
}
