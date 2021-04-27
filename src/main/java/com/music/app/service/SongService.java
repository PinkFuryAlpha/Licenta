package com.music.app.service;

import com.music.app.config.exception.BusinessException;
import com.music.app.dto.SongSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface SongService {
    Long saveSong(SongSaveDto songSaveDto, MultipartFile photo, MultipartFile file) throws BusinessException, IOException;
}
