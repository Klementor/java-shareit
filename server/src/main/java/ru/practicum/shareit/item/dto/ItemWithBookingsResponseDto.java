package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemWithBookingsResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    @Getter
    @Setter
    public static class BookingDto {
        private Long id;
        private Long bookerId;

        public static BookingDto fromBooking(Booking booking) {
            BookingDto bookingDto = new BookingDto();

            bookingDto.setId(booking.getId());
            bookingDto.setBookerId(booking.getBooker().getId());

            return bookingDto;
        }
    }

    @Getter
    @Setter
    public static class CommentDto {
        private Long id;
        private String text;
        private String authorName;
        private LocalDateTime created;

        public static List<CommentDto> toListCommentDto(List<Comment> comments) {
            List<CommentDto> commentDtoList = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDto commentDto = new CommentDto();

                commentDto.id = comment.getId();
                commentDto.text = comment.getText();
                commentDto.authorName = comment.getAuthor().getName();
                commentDto.created = comment.getTime();

                commentDtoList.add(commentDto);
            }
            return commentDtoList;
        }
    }
}
