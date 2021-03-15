package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.UserLoginDTO;
import com.music.app.dto.UserRegisterDTO;
import com.music.app.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Long save(UserRegisterDTO userRegisterDTO) throws BusinessException;

    User confirmRegistration(String token) throws BusinessException;

    String login(UserLoginDTO userLoginDTO) throws BusinessException;
}
