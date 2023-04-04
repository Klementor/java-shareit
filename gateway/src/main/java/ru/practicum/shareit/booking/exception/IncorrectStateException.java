package ru.practicum.shareit.booking.exception;

import javax.validation.ConstraintDeclarationException;

public class IncorrectStateException extends ConstraintDeclarationException {
    private static final String UNKNOWN_STATE;

    static {
        UNKNOWN_STATE = "Unknown state: %s";
    }

    public IncorrectStateException(String message) {
        super(message);
    }

    public static IncorrectStateException fromString(String state) {
        String message = String.format(UNKNOWN_STATE, state);
        return new IncorrectStateException(message);
    }
}
