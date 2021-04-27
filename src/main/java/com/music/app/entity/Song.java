package com.music.app.entity;

import com.music.app.dto.Genre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "song_name",nullable = false)
    private String songName;

    @Column(name = "song_duration",nullable = false)
    private Double duration;

    @Column(name = "views",nullable = false)
    private Long views;

    @Column(name = "music_genre",nullable = false)
    private Genre genre;

    @Column(name = "upVotes")
    private Long upVotes;

    @Column(name = "cover_photo_path", nullable = false)
    private String coverPhotoStoreLocation;

    @Column(name = "song_storage_location", nullable = false)
    private String musicStoreLocation;

    @ManyToMany(mappedBy = "songsCreated")
    private Set<User> artists;

    public Song() {
    }

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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public String getCoverPhotoStoreLocation() {
        return coverPhotoStoreLocation;
    }

    public void setCoverPhotoStoreLocation(String coverPhotoStoreLocation) {
        this.coverPhotoStoreLocation = coverPhotoStoreLocation;
    }

    public String getMusicStoreLocation() {
        return musicStoreLocation;
    }

    public void setMusicStoreLocation(String musicStoreLocation) {
        this.musicStoreLocation = musicStoreLocation;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Set<User> getArtists() {
        return artists;
    }

    public void setArtists(Set<User> artists) {
        this.artists = artists;
    }
}
