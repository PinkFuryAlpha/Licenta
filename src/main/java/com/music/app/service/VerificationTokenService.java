package com.music.app.service;

import com.music.app.entity.User;
import com.music.app.entity.VerificationToken;

public interface VerificationTokenService {

     VerificationToken saveVerificationToken(User user, String type);

     VerificationToken findByToken(String token);

     void removeToken(String token);
}
