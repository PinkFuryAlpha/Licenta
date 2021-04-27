package com.music.app.constraint;

import com.music.app.validators.PhotoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhotoValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhotoConstraint {
    String message() default "Photo can't be empty!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
