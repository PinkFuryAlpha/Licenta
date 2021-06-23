package com.music.app.repo;

import com.music.app.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Playlist p WHERE p.user_id= ?1 AND p.id = ?2", nativeQuery = true)
    void deletePlayList(Long userId, Long songId);
}
