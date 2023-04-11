package ru.practicum.shareit.booking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.validation.annotation.StartBeforeEndValid;
import ru.practicum.shareit.validation.group.CreationGroup;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@StartBeforeEndValid(groups = {CreationGroup.class})
public class BookItemRequestDto {
    @NotNull(groups = {CreationGroup.class})
    private Long itemId;

    @NotNull(groups = {CreationGroup.class})
    @FutureOrPresent(groups = {CreationGroup.class})
    private LocalDateTime start;

    @NotNull(groups = {CreationGroup.class})
    @FutureOrPresent(groups = {CreationGroup.class})
    private LocalDateTime end;
}
