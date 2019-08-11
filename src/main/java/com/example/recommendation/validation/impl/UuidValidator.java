package com.example.recommendation.validation.impl;

import com.example.recommendation.validation.Uuid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

    private static final String UUID_REGEXP = "[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        return value.matches(UUID_REGEXP);
    }
}