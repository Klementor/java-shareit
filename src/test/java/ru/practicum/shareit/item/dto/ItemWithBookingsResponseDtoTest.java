package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

class ItemWithBookingsResponseDtoTest {
    /**
     * Method under test: {@link ItemWithBookingsResponseDto.BookingDto#fromBooking(Booking)}
     */
    @Test
    void testBookingDtoFromBooking() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        ItemWithBookingsResponseDto.BookingDto actualFromBookingResult = ItemWithBookingsResponseDto.BookingDto
                .fromBooking(booking);
        assertEquals(1L, actualFromBookingResult.getBookerId().longValue());
        assertEquals(1L, actualFromBookingResult.getId().longValue());
    }

    /**
     * Method under test: {@link ItemWithBookingsResponseDto.CommentDto#toListCommentDto(List)}
     */
    @Test
    void testCommentDtoToListCommentDto() {
        assertTrue(ItemWithBookingsResponseDto.CommentDto.toListCommentDto(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link ItemWithBookingsResponseDto.CommentDto#toListCommentDto(List)}
     */
    @Test
    void testCommentDtoToListCommentDto2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setId(1L);
        comment.setItem(item);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        List<ItemWithBookingsResponseDto.CommentDto> actualToListCommentDtoResult = ItemWithBookingsResponseDto.CommentDto
                .toListCommentDto(commentList);
        assertEquals(1, actualToListCommentDtoResult.size());
        ItemWithBookingsResponseDto.CommentDto getResult = actualToListCommentDtoResult.get(0);
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("Text", getResult.getText());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("0001-01-01", getResult.getCreated().toLocalDate().toString());
    }
}

