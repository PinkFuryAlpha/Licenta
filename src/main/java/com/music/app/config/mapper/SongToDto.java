package com.music.app.config.mapper;

import com.music.app.dto.SongStreamDto;
import com.music.app.entity.Song;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SongToDto {

    public static SongStreamDto convertEntityToStreamDto(Song song) {
        SongStreamDto streamDto = new SongStreamDto();
        streamDto.setId(song.getId());
        streamDto.setSongName(song.getSongName());
        streamDto.setGenre(song.getGenre());
        streamDto.setViews(song.getViews() + 1);
        streamDto.setUpVotes(song.getUpVotes());

        Set<String> artists = song.getArtists()
                .stream()
                .map(artist -> artist.getFirstName() + " " + artist.getLastName())
                .collect(Collectors.toSet());

        streamDto.setArtists(artists);
        return streamDto;
    }
}
