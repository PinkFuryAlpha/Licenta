package com.music.app.dto;

import java.util.Set;

public class SongStreamDto {
    private Long id;

    private String songName;

    private Genre genre;

    private Long upVotes;

    private Long views;

    private Set<String> artists;

    private Long photoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
}
