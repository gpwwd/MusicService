package com.musicservice.controller;


import com.musicservice.musicservice.DefaultSongStorageServiceImpl;
import com.musicservice.service.SongStorageService;
import com.musicservice.util.AudioFileResponse;
import com.musicservice.util.Range;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Autowired
    public AudioController(final DefaultSongStorageServiceImpl songStorageService) {
        this.songStorageService = songStorageService;
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
                            content = @Content(schema = @Schema(implementation = AudioFileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file upload")
            }
    )
    @PostMapping
    public ResponseEntity<AudioFileResponse> save(@RequestParam("file") MultipartFile file) {
        UUID fileUuid = songStorageService.save(file);
        String originalFilename = file.getOriginalFilename();
        AudioFileResponse response = new AudioFileResponse(fileUuid, originalFilename);
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

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return  "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }
}
