package ru.practicum.shareit.validation.annotation.validator;

import ru.practicum.shareit.validation.annotation.NotEmptyIfNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyIfNotNullConstraintValidator
        implements ConstraintValidator<NotEmptyIfNotNull, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        return (str == null) || (str.length() != 0);
    }
}
