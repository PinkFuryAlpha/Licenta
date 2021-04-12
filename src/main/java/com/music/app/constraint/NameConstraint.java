package com.music.app.constraint;

import com.music.app.validators.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    String message() default "Invalid name. Should not contain any digits or special characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
