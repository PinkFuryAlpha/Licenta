package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.constraint.MusicFileConstraint;
import com.music.app.dto.SongSaveDto;
import com.music.app.entity.User;
import com.music.app.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URI;


@RestController
@RequestMapping(path = "/songs")
@Validated
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping()
    public ResponseEntity<User> saveSong(@RequestPart("cover-photo") MultipartFile photo, @RequestPart("music-file") @MusicFileConstraint MultipartFile file, @RequestPart("song-details") final SongSaveDto songSaveDto) throws BusinessException, IOException {
        long id = songService.saveSong(songSaveDto, photo, file);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
