package com.music.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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

    @Column(name = "profile_picture_path", nullable = false)
    private String profilePictureStoreLocation;

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

    public String getProfilePictureStoreLocation() {
        return profilePictureStoreLocation;
    }

    public void setProfilePictureStoreLocation(String profilePictureStoreLocation) {
        this.profilePictureStoreLocation = profilePictureStoreLocation;
    }
}
