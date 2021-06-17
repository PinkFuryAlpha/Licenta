package com.music.app.config.mapper;

import com.music.app.dto.LoginDto;
import com.music.app.entity.User;

public class UserToDto {

    public static LoginDto convertEntityToLoginDto(User user, String jwt) {
        LoginDto loginDto = new LoginDto();
        loginDto.setId(user.getId());
        loginDto.setFirstName(user.getFirstName());
        loginDto.setLastName(user.getLastName());
        loginDto.setUsername(user.getUsername());
        //TODO: fix bug on login if user has no photo
        if (user.getProfilePicture() == null) {
            loginDto.setPhotoId(1L);
        } else {
            loginDto.setPhotoId(user.getProfilePicture().getId());
        }
        loginDto.setJwtToken(jwt);
        loginDto.setRoles(user.getRoles());
        loginDto.setEmail(user.getEmail());

        return loginDto;
    }
}
