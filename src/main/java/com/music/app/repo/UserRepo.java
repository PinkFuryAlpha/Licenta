package com.music.app.repo;

import com.music.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(final String email);

    User findByUsername(final String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users a SET a.password= ?1 WHERE a.id= ?2", nativeQuery = true)
    void updatePassword(String password, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users a SET a.enabled = TRUE WHERE a.email = ?1", nativeQuery = true)
    void enableUser(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Users a WHERE a.enabled = 0", nativeQuery = true)
    void deleteExpiredUsers();
}
