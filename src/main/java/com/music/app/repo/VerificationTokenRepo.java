package com.music.app.repo;

import com.music.app.entity.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(final String token);

    Long removeByToken(String token);


}
