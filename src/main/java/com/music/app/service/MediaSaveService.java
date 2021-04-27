package com.music.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaSaveService {
    public String saveMedia(MultipartFile media, String path) throws IOException ;
}
