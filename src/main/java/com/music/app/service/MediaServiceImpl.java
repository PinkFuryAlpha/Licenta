package com.music.app.service;

import com.music.app.entity.Photo;
import com.music.app.entity.Song;
import com.music.app.repo.MediaRepository;
import com.music.app.repo.PhotoRepository;
import com.music.app.repo.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MediaServiceImpl implements MediaService {

    private final SongRepository songRepository;

    private final MediaRepository mediaRepository;

    private final PhotoRepository photoRepository;

    @Autowired
    public MediaServiceImpl(SongRepository songRepository,
                            MediaRepository mediaRepository, PhotoRepository photoRepository) {
        this.songRepository = songRepository;
        this.mediaRepository = mediaRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public String saveMedia(MultipartFile media, String path) throws IOException {
        byte[] file = media.getBytes();

        return mediaRepository.save(file, media.getOriginalFilename(), path);
    }

    @Override
    public InputStream getSong(Long songId) throws FileNotFoundException {
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        songRepository.incrementViews(song.getId());

        return new FileInputStream(song.getMusicStoreLocation());
    }

    @Override
    @Transactional
    public void deleteSong(Long songId) {
        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        mediaRepository.deleteMedia(song.getSongCoverPhoto().getPhotoStoreLocation());
        mediaRepository.deleteMedia(song.getMusicStoreLocation());
        System.out.println();

    }

    @Override
    public FileSystemResource getPhoto(Long photoId){
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

         return mediaRepository.fetchMedia(photo.getPhotoStoreLocation());
    }

    @Override
    public InputStream getImage(Long photoId) throws FileNotFoundException {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new FileInputStream(photo.getPhotoStoreLocation());
    }

    @Override
    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        mediaRepository.deleteMedia(photo.getPhotoStoreLocation());
    }

}
