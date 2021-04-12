package com.music.app.validators;

import com.music.app.config.constants.Constants;
import com.music.app.constraint.NameConstraint;
import com.music.app.constraint.UsernameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {

    @Override
    public void initialize(UsernameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return  username.matches("^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$") && username.length()< Constants.USERNAME_LENGTH;
    }
}
