package com.music.app.service;

import com.music.app.entity.User;
import com.music.app.config.tokens.VerificationToken;
import com.music.app.repo.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//String token = UUID.randomUUID().toString();
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;


    @Override
    public VerificationToken saveVerificationToken(User user) {
        VerificationToken token = new VerificationToken(user, LocalDateTime.now().plusSeconds(2800));
        verificationTokenRepo.save(token);
        return token;
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepo.findByToken(token);
    }

    @Override
    public void removeToken(String token){
        verificationTokenRepo.removeByToken(token);
    }
}
