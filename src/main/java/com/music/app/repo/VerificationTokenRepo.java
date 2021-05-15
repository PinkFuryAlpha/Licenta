package com.music.app.repo;

import com.music.app.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(final String token);

    Long removeByToken(String token);

    @Modifying
    @Query("DELETE FROM VerificationToken v WHERE v.expireAt <= ?1")
    void deleteAllExpiredTokens(LocalDateTime now);
}
