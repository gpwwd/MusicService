package com.musicservice.service;

import com.musicservice.dto.get.CommentGetDto;
import com.musicservice.dto.post.SongPostDto;
import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.post.SongWithImageAndAudioIdPostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SongService {
    List<SongGetDto> getAll();
    SongGetDto getById(int id);
    List<CommentGetDto> getCommentsBySongId(int id);
    SongPostDto save(SongPostDto songDto);
    SongPostDto save(SongWithImageAndAudioIdPostDto songDto, MultipartFile cover);
    SongPostDto updateSong(int id, SongPostDto songDto);
    void deleteSong(int id);
}
