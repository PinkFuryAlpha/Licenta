package com.music.app.validators;

import com.music.app.constraint.PhotoConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhotoValidator implements ConstraintValidator<PhotoConstraint, MultipartFile> {
    @Override
    public void initialize(PhotoConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile photo, ConstraintValidatorContext context) {
        boolean result = true;

        String contentType = photo.getContentType();

        if (photo.getSize() < 0 || photo.isEmpty()) {
            result = false;
        }

        if (contentType != null && !isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Image format is allowed.")
                    .addConstraintViolation();

            result = false;
        }

        if (photo.getSize() > 10 * 1024 * 1024) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File can't pe greater than 10Mb")
                    .addConstraintViolation();
            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}

