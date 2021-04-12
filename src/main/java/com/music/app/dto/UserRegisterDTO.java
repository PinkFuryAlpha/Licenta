package com.music.app.dto;

import com.music.app.constraint.NameConstraint;
import com.music.app.constraint.PasswordConstraint;
import com.music.app.constraint.UsernameConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRegisterDTO {

    @NotBlank(message = "First Name can't be empty")
    @NameConstraint
    private String firstName;

    @NotBlank
    @NameConstraint
    private String lastName;

    @NotBlank
    @UsernameConstraint
    private String username;

    @NotBlank
    @NotNull
    @Email(message = "Should respect ***@*** format")
    private String email;

    @NotBlank(message = "Password should be at least 8 characters long")
    @PasswordConstraint
    private String password;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
