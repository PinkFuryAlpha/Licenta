package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.PasswordResetDto;
import com.music.app.dto.UserLoginDTO;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService extends UserDetailsService {

    Long save(UserRegisterDTO userRegisterDTO) throws BusinessException;

    User confirmRegistration(String token) throws BusinessException;

    String login(UserLoginDTO userLoginDTO) throws BusinessException;

    String forgotPassword(String email) throws BusinessException;

    void resetPassword(String passwordResetToken, PasswordResetDto password) throws BusinessException;

    Long saveProfilePicture(MultipartFile multipartFile, HttpServletRequest request) throws BusinessException, IOException;

    void updateUserToArtist(HttpServletRequest request);

}
