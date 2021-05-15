package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.config.mapper.SongToDto;
import com.music.app.dto.SongSaveDto;
import com.music.app.dto.SongStreamDto;
import com.music.app.entity.Song;
import com.music.app.entity.User;
import com.music.app.dto.pageables.SongDto;
import com.music.app.repo.SongRepository;
import com.music.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final UserRepo userRepo;

    private final MediaService mediaService;

    @Value("${song.photo.path}")
    private String photoCover;

    @Value("${song.media.path}")
    private String music;

    @Autowired
    public SongServiceImpl(SongRepository songRepository, UserRepo userRepo, MediaService mediaService) {
        this.songRepository = songRepository;
        this.userRepo = userRepo;
        this.mediaService = mediaService;
    }

    @Override
    public Long saveSong(SongSaveDto songSaveDto, MultipartFile photo, MultipartFile file, HttpServletRequest request) throws IOException, BusinessException {
        Song song = new Song();
        Set<User> artists = new LinkedHashSet<>();
        Principal principal = request.getUserPrincipal();

        User creator = userRepo.findByUsername(principal.getName());
        artists.add(creator);
        creator.getSongsCreated().add(song);

        songSaveDto.getArtists().stream().map(userRepo::findByUsername).forEach(user -> {
            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ARTIST"))) {
                user.getSongsCreated().add(song);
                artists.add(user);
            } else {
                try {
                    throw new BusinessException(400, "The user is not an artist.");
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });

        String coverPhotoPath = mediaService.saveMedia(photo, this.photoCover);

        String musicPath = mediaService.saveMedia(file, this.music);

        System.out.println(file.getContentType());

        song.setSongName(songSaveDto.getSongName());
        song.setGenre(songSaveDto.getGenre());
        song.setCoverPhotoStoreLocation(coverPhotoPath);
        song.setMusicStoreLocation(musicPath);
        song.setUpVotes(0);
        song.setViews(0L);
        song.setArtists(artists);


        return songRepository.save(song).getId();
    }

    @Override
    public Page<SongStreamDto> getSongs(SongDto songDto) {
        Sort sort = Sort.by(songDto.getSortDirection(), songDto.getSortBy());

        Pageable pageable = PageRequest.of(songDto.getPageNumber(),
                songDto.getPageSize(),
                sort);

        Page<Song> songs = songRepository.findAll(pageable);

        return songs.map(new Function<Song, SongStreamDto>() {
            @Override
            public SongStreamDto apply(Song song) {
                return SongToDto.convertEntityToStreamDto(song);
            }
        });

    }

    @Override
    public SongStreamDto getSong(Long songId) throws BusinessException {
        Song song = songRepository
                .findById(songId)
                .orElseThrow(() -> new BusinessException(404, "Song was not found, or probably deleted."));

        songRepository.incrementViews(songId);

        return SongToDto.convertEntityToStreamDto(song);
    }

    @Transactional
    @Override
    public void likeSong(Long songId, HttpServletRequest request) throws BusinessException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Song song = songRepository
                .findById(songId)
                .orElseThrow(() -> new BusinessException(404, "Song was not found, or probably deleted."));

        if (song.getUsersWhoLiked().contains(user)) {
            throw new BusinessException(400, "User already liked!");
        }
        song.getUsersWhoLiked().add(user);
        user.getLikedSongs().add(song);
        song.setUpVotes(song.getUpVotes() + 1);
    }

    @Transactional
    @Override
    public void unlikeSong(Long songId, HttpServletRequest request) throws BusinessException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Song song = songRepository
                .findById(songId)
                .orElseThrow(() -> new BusinessException(404, "Song was not found, or probably deleted."));

        if (song.getUsersWhoLiked().contains(user)) {
            song.getUsersWhoLiked().remove(user);
            user.getLikedSongs().remove(song);
            song.setUpVotes(song.getUpVotes() - 1);
        } else {
            throw new BusinessException(400, "User did not like the song.");
        }
    }

    @Transactional
    public void deleteSong(Long songId,HttpServletRequest request) throws BusinessException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());

        Song song = songRepository
                .findById(songId)
                .orElseThrow(() -> new BusinessException(404, "Song was not found, or probably deleted."));

        if(!song.getArtists().contains(user)){
            throw new BusinessException(401,"The user is not an contributor.");
        }

        mediaService.deleteSong(songId);
        songRepository.delete(song);
    }
}
