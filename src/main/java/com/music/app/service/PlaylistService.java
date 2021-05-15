package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.PlaylistCreateDto;
import com.music.app.dto.PlaylistDto;
import com.music.app.entity.Playlist;

import javax.servlet.http.HttpServletRequest;

public interface PlaylistService {
    PlaylistDto getPlaylist(Long playlistId) throws BusinessException;

    Long createPlaylist(PlaylistCreateDto playlist, HttpServletRequest request);

    void addSongToPlayList(Long songId, Long playlistId, HttpServletRequest request) throws BusinessException;
}
