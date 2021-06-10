package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.SongSaveDto;
import com.music.app.dto.SongStreamDto;
import com.music.app.dto.pageables.SongDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SongService {
    Long saveSong(SongSaveDto songSaveDto, MultipartFile photo, MultipartFile file, HttpServletRequest request) throws IOException, BusinessException;

    SongStreamDto getSong(Long songId) throws BusinessException;

    Page<SongStreamDto> getSongs(SongDto songDto);

    void likeSong(Long songId, HttpServletRequest request) throws BusinessException;

    void unlikeSong(Long songId, HttpServletRequest request) throws BusinessException;

    void deleteSong(Long songId, HttpServletRequest request) throws BusinessException;

    Boolean isSongLiked(Long songId, HttpServletRequest request) throws BusinessException;

    Page<SongStreamDto> getLikedSongsByUser(SongDto songDto, HttpServletRequest request);

    Set<SongStreamDto> getUserSongs(String username);
}
