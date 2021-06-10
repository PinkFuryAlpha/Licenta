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
        loginDto.setPhotoId(1L);
        loginDto.setJwtToken(jwt);
        loginDto.setRoles(user.getRoles());

        return loginDto;
    }
}
