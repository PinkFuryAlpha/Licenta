package com.music.app.dto;

import com.music.app.constraint.NameConstraint;
import com.music.app.entity.User;

import java.util.Set;

public class SongSaveDto {

    @NameConstraint
    private String songName;

    private Genre genre;

    private Set<String> artists;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<String> getArtists() {
        return artists;
    }

    public void setArtists(Set<String> artists) {
        this.artists = artists;
    }
}
