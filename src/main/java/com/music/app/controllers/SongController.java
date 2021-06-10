package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.constraint.MusicFileConstraint;
import com.music.app.constraint.PhotoConstraint;
import com.music.app.dto.SongSaveDto;
import com.music.app.dto.SongStreamDto;
import com.music.app.dto.pageables.SongDto;
import com.music.app.entity.Song;
import com.music.app.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Set;


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
    public ResponseEntity<Song> saveSong(@RequestPart("cover-photo") @PhotoConstraint MultipartFile photo,
                                         @RequestPart("music-file") @MusicFileConstraint MultipartFile file,
                                         @RequestPart("song-details") final SongSaveDto songSaveDto,
                                         HttpServletRequest request) throws BusinessException, IOException {
        long id = songService.saveSong(songSaveDto, photo, file, request);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "/getAllSongs")
    public ResponseEntity<Page<SongStreamDto>> getAllSongs(@RequestBody final SongDto songDto) {
        return ResponseEntity.ok(songService.getSongs(songDto));
    }

    @GetMapping(path="/getUserSongs")
    public ResponseEntity<Set<SongStreamDto>> getUserSongs(@RequestParam final String username){
        return ResponseEntity.ok(songService.getUserSongs(username));
    }

    @GetMapping(path = "/get-song")
    public ResponseEntity<SongStreamDto> getSong(@RequestParam final Long songId) throws BusinessException {
        return ResponseEntity.ok(songService.getSong(songId));
    }

    @PostMapping(path = "/getUserLikedSongs")
    public ResponseEntity<Page<SongStreamDto>> getLikedSongsByUser(@RequestBody final SongDto songDto, HttpServletRequest request) {
        return ResponseEntity.ok(songService.getLikedSongsByUser(songDto, request));
    }

    @PostMapping(path = "/like")
    public ResponseEntity<String> likeSong(@RequestParam final Long songId, HttpServletRequest request) throws BusinessException {
        songService.likeSong(songId, request);
        return ResponseEntity.ok("Song has been liked.");
    }

    @PostMapping(path = "/unLike")
    public ResponseEntity<String> unlikeSong(@RequestParam final Long songId, HttpServletRequest request) throws BusinessException {
        songService.unlikeSong(songId, request);
        return ResponseEntity.ok("Song has been unliked.");
    }

    @GetMapping(path = "/isSongLikedByUser")
    public ResponseEntity<Boolean> isSongLiked(@RequestParam final Long songId, HttpServletRequest request) throws BusinessException {
        songService.isSongLiked(songId, request);
        return ResponseEntity.ok(songService.isSongLiked(songId, request));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteSong(@RequestParam final Long songId, HttpServletRequest request) throws BusinessException {
        songService.deleteSong(songId, request);
        return ResponseEntity.ok("Song has been deleted.");
    }

}
