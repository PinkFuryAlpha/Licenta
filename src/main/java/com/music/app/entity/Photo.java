package com.music.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "profile_picture")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @OneToOne(mappedBy = "profilePicture")
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "songCoverPhoto")
    @JoinColumn(name = "album_id")
    private Song song;

    @Column(name = "profile_picture_path", nullable = false)
    private String photoStoreLocation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoStoreLocation() {
        return photoStoreLocation;
    }

    public void setPhotoStoreLocation(String photoStoreLocation) {
        this.photoStoreLocation = photoStoreLocation;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
