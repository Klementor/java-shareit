package ru.practicum.shareit.item.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DetailedItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class CommentDto {
        private Long id;
        private String authorName;
        private String text;
        private LocalDateTime created;
    }
}
