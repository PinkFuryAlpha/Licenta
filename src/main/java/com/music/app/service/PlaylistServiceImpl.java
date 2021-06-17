package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.config.mapper.PlaylistToDto;
import com.music.app.dto.PlaylistCreateDto;
import com.music.app.dto.PlaylistDto;
import com.music.app.entity.Playlist;
import com.music.app.entity.Song;
import com.music.app.entity.User;
import com.music.app.repo.PlaylistRepository;
import com.music.app.repo.SongRepository;
import com.music.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final UserRepo userRepo;

    private final SongRepository songRepository;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, UserRepo userRepo, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepo = userRepo;
        this.songRepository = songRepository;
    }

    @Override
    public PlaylistDto getPlaylist(Long playlistId) throws BusinessException {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new BusinessException(404, "Playlist not found"));

        return PlaylistToDto.convertEntityToPlaylistDto(playlist);
    }

    @Override
    public Set<PlaylistDto> getAllUserPlaylists(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Set<Playlist> userPlaylists= user.getPlaylists();
        Set<PlaylistDto> response = new HashSet<>();

        for (Playlist userPlaylist : userPlaylists) {
            response.add(PlaylistToDto.convertEntityToPlaylistDto(userPlaylist));
        }

        return response;

    }

    @Override
    public Long createPlaylist(PlaylistCreateDto playlist, HttpServletRequest request){
        Playlist newPlaylist = new Playlist();

        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        newPlaylist.setAlbumName(playlist.getName());
        newPlaylist.setSongs(Collections.emptySet());
        newPlaylist.setOwner(user);

        return playlistRepository.save(newPlaylist).getId();
    }

    //TO DO add/remove song to playlist
    @Override
    public void addSongToPlayList(Long songId, Long playlistId, HttpServletRequest request) throws BusinessException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Song song = songRepository
                .findById(songId)
                .orElseThrow(() -> new BusinessException(404, "Song was not found, or probably deleted."));

        Playlist playlist = playlistRepository
                .findById(playlistId)
                .orElseThrow(() -> new BusinessException(404, "Playlist was not found, or probably deleted."));

        if(!playlist.getOwner().equals(user)){
            throw new BusinessException(404,"The playlist doesn't belong to the user.");
        }

        song.getPlaylists().add(playlist);
        playlist.getSongs().add(song);
    }
}
