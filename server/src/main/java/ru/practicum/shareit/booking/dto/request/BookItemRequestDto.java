package ru.practicum.shareit.booking.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookItemRequestDto {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
