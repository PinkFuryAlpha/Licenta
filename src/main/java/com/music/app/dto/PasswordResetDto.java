package com.music.app.dto;

import com.music.app.constraint.PasswordConstraint;

public class PasswordResetDto {
    @PasswordConstraint
    private String password;

    public PasswordResetDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
