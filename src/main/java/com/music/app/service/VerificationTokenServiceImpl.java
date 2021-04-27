package com.music.app.service;

import com.music.app.config.constants.Constants;
import com.music.app.entity.User;
import com.music.app.entity.VerificationToken;
import com.music.app.repo.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

//String token = UUID.randomUUID().toString();
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;


    @Override
    public VerificationToken saveVerificationToken(User user, String type) {
        VerificationToken token = new VerificationToken(user, LocalDateTime.now().plusSeconds(Constants.USER_VERIFICATION_EXPIRATION));
        token.setTokenType(type);
        verificationTokenRepo.save(token);
        return token;
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepo.findByToken(token);
    }

    @Override
    public void removeToken(String token) {
        verificationTokenRepo.removeByToken(token);
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredTokens() {
        System.out.println("Deleted all expired tokens.");
        LocalDateTime now = LocalDateTime.now();
        verificationTokenRepo.deleteAllExpiredTokens(now);
    }

}
