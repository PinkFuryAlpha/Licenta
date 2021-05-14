package com.music.app.controllers;


import com.music.app.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;


@RestController
@RequestMapping(path = "/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping(path = "/fetch-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getImage(@RequestParam final Long songId) throws Exception {
        return ResponseEntity.ok(mediaService.findMedia(songId));
    }

    @GetMapping(path = "/getSong")
    public ResponseEntity<InputStreamResource> getSong(@RequestParam final Long songId) throws Exception {
        InputStream inputStream = mediaService.getSong(songId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-ranges", "bytes");
        headers.set("Content-Type", "audio/mpeg");
        headers.set("Content-Range", " bytes 50-1025/17839845");
        return new ResponseEntity<>(new InputStreamResource(inputStream),headers, HttpStatus.OK);
    }

}
