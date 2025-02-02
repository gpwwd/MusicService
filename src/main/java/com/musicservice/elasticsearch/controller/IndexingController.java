package com.musicservice.elasticsearch.controller;

import com.musicservice.elasticsearch.service.SongIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elastic")
public class IndexingController {

    private final SongIndexingService songIndexingService;

    @Autowired
    public IndexingController(SongIndexingService songIndexingService) {
        this.songIndexingService = songIndexingService;
    }

    @GetMapping("/reindex")
    public String reindexSongs() {
        songIndexingService.reindexAllSongs();
        return "Переиндексация завершена!";
    }
}
