package com.music.app.constraint;

import com.music.app.validators.MusicValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = MusicValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MusicFileConstraint {
        String message() default "Is not a valid file!";

        long value () default (1024 * 1024 * 100);

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
