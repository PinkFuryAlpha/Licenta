package com.music.app.service;

import com.music.app.repo.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MediaSaveServiceImpl implements MediaSaveService {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaSaveServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public String saveMedia(MultipartFile media, String path) throws IOException {
        byte[] file = media.getBytes();

        return mediaRepository.save(file, media.getOriginalFilename(), path);
    }
}
