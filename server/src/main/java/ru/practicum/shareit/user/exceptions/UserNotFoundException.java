package ru.practicum.shareit.user.exceptions;

import ru.practicum.shareit.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
