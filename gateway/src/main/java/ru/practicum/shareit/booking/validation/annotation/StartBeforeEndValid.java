package ru.practicum.shareit.booking.validation.annotation;

import ru.practicum.shareit.booking.validation.annotation.validator.StartBeforeEndDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StartBeforeEndDateValidator.class)
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEndValid {
    String message() default "Start must be before end and start/end must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
