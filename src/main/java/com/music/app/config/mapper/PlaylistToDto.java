package com.music.app.config.mapper;

import com.music.app.dto.PlaylistDto;
import com.music.app.dto.SongStreamDto;
import com.music.app.entity.Playlist;

import java.util.Set;
import java.util.stream.Collectors;

public class PlaylistToDto {

    public static PlaylistDto convertEntityToPlaylistDto(Playlist playlist){
        PlaylistDto playlistDto = new PlaylistDto();
        Set<SongStreamDto> songStreamDtos= playlist.getSongs().stream().map(SongToDto::convertEntityToStreamDto).collect(Collectors.toSet());
        playlistDto.setId(playlist.getId());
        playlistDto.setAlbumName(playlist.getAlbumName());
        playlistDto.setOwnerId(playlist.getOwner().getId());
        playlistDto.setSongs(songStreamDtos);

        return playlistDto;
    }
}
