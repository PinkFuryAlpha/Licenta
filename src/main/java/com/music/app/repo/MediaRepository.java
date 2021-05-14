package com.music.app.repo;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class MediaRepository {

    public String save(byte[] media, String name, String path) throws IOException {
        Path savePath = Paths.get(path + new Date().getTime() + "-" + name);

        Files.write(savePath,media);

        return savePath.toAbsolutePath().toString();
    }

    public FileSystemResource fetchMedia(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
