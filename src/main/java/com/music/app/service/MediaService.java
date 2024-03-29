package com.music.app.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface MediaService {
    String saveMedia(MultipartFile media, String path) throws IOException;

    InputStream getSong(Long songId) throws FileNotFoundException;

    void deleteSong(Long songId);

    void deletePhoto(Long photoId);

    FileSystemResource getPhoto(Long photoId);

    public InputStream getImage(Long photoId) throws FileNotFoundException;
}
