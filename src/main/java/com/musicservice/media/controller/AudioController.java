package com.musicservice.media.controller;


import com.musicservice.catalog.service.SongService;
import com.musicservice.media.service.DefaultSongStorageServiceImpl;
import com.musicservice.media.service.SongStorageService;
import com.musicservice.media.dto.AudioFileMetadataResponse;
import com.musicservice.media.util.Range;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(
            summary = "Upload an audio file",
            description = "Uploads an audio file and returns its metadata",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "File to upload",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "object", format = "binary")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                            content = @Content(schema = @Schema(implementation = AudioFileMetadataResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file upload")
            }
    )
    @PostMapping
    public ResponseEntity<AudioFileMetadataResponse> save(@RequestParam("file") MultipartFile file) {
        UUID fileUuid = songStorageService.save(file);
        String originalFilename = file.getOriginalFilename();
        AudioFileMetadataResponse response = new AudioFileMetadataResponse(fileUuid, originalFilename);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/{id}/metadata")
    public ResponseEntity<AudioFileMetadataResponse> getAudioFileMetadataBySongId(@PathVariable("id") int id) {
        AudioFileMetadataResponse metadata = songService.getAudioFileMetadataBySongId(id);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return  "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

}
