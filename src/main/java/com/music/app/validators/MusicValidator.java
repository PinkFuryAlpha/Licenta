package com.music.app.validators;

import com.music.app.constraint.MusicFileConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MusicValidator implements ConstraintValidator<MusicFileConstraint, MultipartFile> {

    @Override
    public void initialize(MusicFileConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile song, ConstraintValidatorContext context) {
        boolean result = true;

        String contentType = song.getContentType();
        if (!isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only mp3 format is allowed.")
                    .addConstraintViolation();

            result = false;
        }

        if (song.getSize() > 100 * 1024 * 1024) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File can't pe greater than 100Mb")
                    .addConstraintViolation();
            result = false;
        }

        if(song.getSize()<0 || song.isEmpty()){
            result=false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        System.out.println(contentType.equals("audio/mpeg"));
        return contentType.equals("audio/mpeg");
    }
}

