package com.music.app.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface MediaService {
    public String saveMedia(MultipartFile media, String path) throws IOException;

    public FileSystemResource findMedia(Long songId);

    public InputStream getSong(Long songId) throws FileNotFoundException;
}
