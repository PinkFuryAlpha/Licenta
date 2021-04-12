package com.music.app.validators;

import com.music.app.config.constants.Constants;
import com.music.app.constraint.NameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {
    @Override
    public void initialize(NameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name.matches("[a-zA-Z]+") && name.length()< Constants.NAME_LENGTH && name.length()>1;
    }
}
