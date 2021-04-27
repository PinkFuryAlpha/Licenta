package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.SongSaveDto;
import com.music.app.entity.Song;
import com.music.app.entity.User;
import com.music.app.repo.SongRepository;
import com.music.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final UserRepo userRepo;

    private final MediaSaveService mediaSaveService;

    @Value("${song.photo.path}")
    private String photoCover;

    @Value("${song.media.path}")
    private String music;

    @Autowired
    public SongServiceImpl(SongRepository songRepository, UserRepo userRepo, MediaSaveService mediaSaveService) {
        this.songRepository = songRepository;
        this.userRepo = userRepo;
        this.mediaSaveService = mediaSaveService;
    }

    @Override
    public Long saveSong(SongSaveDto songSaveDto, MultipartFile photo, MultipartFile file) throws BusinessException, IOException {
        Song song = new Song();
        Set<User> artist = new HashSet<>();

        songSaveDto.getArtists().forEach(username -> {
            User user = userRepo.findByUsername(username);
            user.getSongsCreated().add(song);
            artist.add(user);
        });

        String coverPhotoPath = mediaSaveService.saveMedia(photo, this.photoCover);

        String musicPath = mediaSaveService.saveMedia(file, this.music);

        System.out.println(file.getContentType());

        song.setSongName(songSaveDto.getSongName());
        song.setDuration(3.69);
        song.setGenre(songSaveDto.getGenre());
        song.setCoverPhotoStoreLocation(coverPhotoPath);
        song.setMusicStoreLocation(musicPath);
        song.setUpVotes(0);
        song.setViews(0L);
        song.setArtists(artist);


        return songRepository.save(song).getId();
    }
}
