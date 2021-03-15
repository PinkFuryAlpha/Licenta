package com.music.app.service;

import com.music.app.entity.User;
import com.music.app.config.tokens.VerificationToken;

public interface VerificationTokenService {

     VerificationToken saveVerificationToken(User user);

     VerificationToken findByToken(String token);

     void removeToken(String token);
}
