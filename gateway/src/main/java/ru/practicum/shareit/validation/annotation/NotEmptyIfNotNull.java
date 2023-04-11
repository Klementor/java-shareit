package ru.practicum.shareit.validation.annotation;

import ru.practicum.shareit.validation.annotation.validator.NotEmptyIfNotNullConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyIfNotNullConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyIfNotNull {
    String message() default "should not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
