package com.music.app.controllers;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.PlaylistCreateDto;
import com.music.app.dto.PlaylistDto;
import com.music.app.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(path = "/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping()
    public ResponseEntity<Long> createPlaylist(@RequestBody @Valid final PlaylistCreateDto playlistCreateDto, HttpServletRequest request){
        long id = playlistService.createPlaylist(playlistCreateDto,request);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping()
    public ResponseEntity<PlaylistDto> getPlaylist(@RequestParam Long playlistId) throws BusinessException {
        return ResponseEntity.ok(playlistService.getPlaylist(playlistId));
    }

    @GetMapping()
    public Set<PlaylistDto> getAllUserPlaylists(HttpServletRequest request)

    @PostMapping( path = "/addSong")
    public ResponseEntity<String> addSongToPlayList(@RequestParam Long songId, @RequestParam Long playlistId, HttpServletRequest request) throws BusinessException {
        playlistService.addSongToPlayList(songId,playlistId,request);
        return ResponseEntity.ok("Song added to playlist");
    }
}
