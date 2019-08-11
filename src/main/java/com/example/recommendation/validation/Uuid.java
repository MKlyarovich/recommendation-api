package com.example.recommendation.validation;

import com.example.recommendation.validation.impl.UuidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface Uuid {
    String message() default "UUID должен содержать 36 символов и соответствовать формату xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
