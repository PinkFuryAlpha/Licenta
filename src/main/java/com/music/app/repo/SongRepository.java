package com.music.app.repo;

import com.music.app.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Song> findById(Long id);

    @Query(value = "SELECT s FROM Song s")
    List<Song> getAllSongs();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Song s SET views = views + 1 WHERE s.id = ?1", nativeQuery = true)
    void incrementViews(Long songId);
}
