package ru.practicum.shareit.booking.validation.annotation;

import ru.practicum.shareit.booking.validation.annotation.validator.BookingStateEnumConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = BookingStateEnumConstraintValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingStateEnum {
    String message() default "should be booking state enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
