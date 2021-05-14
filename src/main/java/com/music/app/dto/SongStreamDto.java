package com.music.app.dto;

import java.util.Set;

public class SongStreamDto {
    private String songName;

    private Genre genre;

    private Double duration;

    private Long upVotes;

    private Long views;

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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Set<String> getArtists() {
        return artists;
    }

    public void setArtists(Set<String> artists) {
        this.artists = artists;
    }
}
