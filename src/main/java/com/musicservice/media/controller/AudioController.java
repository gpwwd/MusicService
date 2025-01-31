package com.musicservice.media.controller;


import com.musicservice.catalog.service.SongService;
import com.musicservice.media.service.DefaultSongStorageServiceImpl;
import com.musicservice.media.service.SongStorageService;
import com.musicservice.media.util.Range;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audio")
public class AudioController {

    private final SongStorageService songStorageService;
    private final SongService songService;

    @Autowired
    public AudioController(final DefaultSongStorageServiceImpl songStorageService,
                           final SongService songService) {
        this.songStorageService = songStorageService;
        this.songService = songService;
    }

    @Value("${photon.streaming.default-chunk-size}")
    public Integer defaultChunkSize;

    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> readChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable UUID uuid
    ) {
        Range parsedRange = Range.parseHttpRangeString(range, defaultChunkSize);
        DefaultSongStorageServiceImpl.ChunkWithMetadata chunkWithMetadata = songStorageService.fetchChunk(uuid, parsedRange);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, chunkWithMetadata.metadata().getHttpContentType())
                .header(CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, chunkWithMetadata.metadata().getSize()))
                .header(CONTENT_RANGE, constructContentRangeHeader(parsedRange, chunkWithMetadata.metadata().getSize()))
                .body(chunkWithMetadata.chunk());
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return  "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

}
