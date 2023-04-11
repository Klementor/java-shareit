package ru.practicum.shareit.booking.validation.annotation.validator;

import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.validation.annotation.StartBeforeEndValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartBeforeEndDateValidator
        implements ConstraintValidator<StartBeforeEndValid, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto bookingDto, ConstraintValidatorContext context) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();

        if (start != null && end != null) {
            return start.isBefore(end);
        } else {
            return false;
        }
    }
}
