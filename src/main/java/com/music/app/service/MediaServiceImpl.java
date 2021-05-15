package com.music.app.service;

import com.music.app.entity.Song;
import com.music.app.repo.MediaRepository;
import com.music.app.repo.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MediaServiceImpl implements MediaService {

    private final SongRepository songRepository;

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, SongRepository songRepository) {
        this.mediaRepository = mediaRepository;
        this.songRepository = songRepository;
    }

    @Override
    public String saveMedia(MultipartFile media, String path) throws IOException {
        byte[] file = media.getBytes();

        return mediaRepository.save(file, media.getOriginalFilename(), path);
    }

    @Override
    public FileSystemResource findMedia(Long songId) {
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return mediaRepository.fetchMedia(song.getCoverPhotoStoreLocation());
    }

    @Override
    public InputStream getSong(Long songId) throws FileNotFoundException {
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new FileInputStream(song.getMusicStoreLocation());
    }

    @Override
    public void deleteSong(Long songId){
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        mediaRepository.deleteMedia(song.getMusicStoreLocation());
        mediaRepository.deleteMedia(song.getCoverPhotoStoreLocation());
    }

}
