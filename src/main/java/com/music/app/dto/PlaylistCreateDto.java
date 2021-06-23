package com.music.app.dto;

import com.music.app.constraint.NameConstraint;

public class PlaylistCreateDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
