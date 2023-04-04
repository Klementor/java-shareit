package ru.practicum.shareit.booking.validation.annotation.validator;

import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.exception.IncorrectStateException;
import ru.practicum.shareit.booking.validation.annotation.BookingStateEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStateEnumConstraintValidator
        implements ConstraintValidator<BookingStateEnum, String> {

    @Override
    public boolean isValid(String possibleState, ConstraintValidatorContext context) {
        try {
            State.valueOf(possibleState);
            return true;
        } catch (IllegalArgumentException ex) {
            throw IncorrectStateException.fromString(possibleState);
        }
    }
}
