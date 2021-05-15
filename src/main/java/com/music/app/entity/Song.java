package com.music.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.music.app.dto.Genre;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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

    @Column(name = "views",nullable = false)
    private Long views;

    @Column(name = "music_genre",nullable = false)
    private Genre genre;

    @Column(name = "upVotes")
    private Long upVotes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo songCoverPhoto;

    @Column(name = "song_storage_location", nullable = false)
    private String musicStoreLocation;

    @ManyToMany(mappedBy = "songsCreated")
    @JsonManagedReference
    private Set<User> artists;

    @ManyToMany(mappedBy = "likedSongs")
    @JsonManagedReference
    private Set<User> usersWhoLiked;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "playlists_songs",
            joinColumns = @JoinColumn(
                    name ="song_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "playlist_id", referencedColumnName = "id"))
    @JsonBackReference
    private Set<Playlist> playlists;

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

    public Set<User> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(Set<User> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    public Photo getSongCoverPhoto() {
        return songCoverPhoto;
    }

    public void setSongCoverPhoto(Photo songCoverPhoto) {
        this.songCoverPhoto = songCoverPhoto;
    }
}
