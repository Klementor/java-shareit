package ru.practicum.shareit.item.exceptions;

import ru.practicum.shareit.exception.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
