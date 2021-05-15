package com.music.app.dto;

import com.music.app.entity.User;

import java.util.Set;

public class PlaylistDto {
    private Long id;

    private String albumName;

    private Set<SongStreamDto> songs;

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Set<SongStreamDto> getSongs() {
        return songs;
    }

    public void setSongs(Set<SongStreamDto> songs) {
        this.songs = songs;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
