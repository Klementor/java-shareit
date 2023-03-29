package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    /**
     * Method under test: {@link ItemMapper#toItemResponseDto(Item)}
     */
    @Test
    void testToItemResponseDto() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);
        ItemResponseDto actualToItemResponseDtoResult = ItemMapper.toItemResponseDto(item);
        assertTrue(actualToItemResponseDtoResult.getAvailable());
        assertEquals(1L, actualToItemResponseDtoResult.getRequestId().longValue());
        assertEquals("Name", actualToItemResponseDtoResult.getName());
        assertEquals(1L, actualToItemResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemResponseDtoResult.getDescription());
    }

    /**
     * Method under test: {@link ItemMapper#toItemResponseDto(Item)}
     */
    @Test
    void testToItemResponseDto2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(null);
        ItemResponseDto actualToItemResponseDtoResult = ItemMapper.toItemResponseDto(item);
        assertTrue(actualToItemResponseDtoResult.getAvailable());
        assertEquals("Name", actualToItemResponseDtoResult.getName());
        assertEquals(1L, actualToItemResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemResponseDtoResult.getDescription());
    }

    /**
     * Method under test: {@link ItemMapper#toItem(ItemRequestDto)}
     */
    @Test
    void testToItem() {
        Item actualToItemResult = ItemMapper.toItem(new ItemRequestDto());
        assertNull(actualToItemResult.getAvailable());
        assertNull(actualToItemResult.getName());
        assertNull(actualToItemResult.getId());
        assertNull(actualToItemResult.getDescription());
    }

    /**
     * Method under test: {@link ItemMapper#fromItemListToItemResponseDtoList(Collection)}
     */
    @Test
    void testFromItemListToItemResponseDtoList() {
        assertTrue(ItemMapper.fromItemListToItemResponseDtoList(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link ItemMapper#fromItemListToItemResponseDtoList(Collection)}
     */
    @Test
    void testFromItemListToItemResponseDtoList2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        List<ItemResponseDto> actualFromItemListToItemResponseDtoListResult = ItemMapper
                .fromItemListToItemResponseDtoList(itemList);
        assertEquals(1, actualFromItemListToItemResponseDtoListResult.size());
        ItemResponseDto getResult = actualFromItemListToItemResponseDtoListResult.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals(1L, getResult.getRequestId().longValue());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }

    /**
     * Method under test: {@link ItemMapper#fromItemListToItemResponseDtoList(Collection)}
     */
    @Test
    void testFromItemListToItemResponseDtoList3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);

        User user2 = new User();
        user2.setEmail("john.smith@example.org");
        user2.setId(2L);
        user2.setName("42");

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(2L);
        user3.setName("42");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("Description");
        request1.setId(2L);
        request1.setRequester(user3);

        Item item1 = new Item();
        item1.setAvailable(false);
        item1.setDescription("Description");
        item1.setId(2L);
        item1.setName("42");
        item1.setOwner(user2);
        item1.setRequest(request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        List<ItemResponseDto> actualFromItemListToItemResponseDtoListResult = ItemMapper
                .fromItemListToItemResponseDtoList(itemList);
        assertEquals(2, actualFromItemListToItemResponseDtoListResult.size());
        ItemResponseDto getResult = actualFromItemListToItemResponseDtoListResult.get(0);
        assertEquals(2L, getResult.getRequestId().longValue());
        ItemResponseDto getResult1 = actualFromItemListToItemResponseDtoListResult.get(1);
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("Name", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        assertTrue(getResult1.getAvailable());
        assertEquals("42", getResult.getName());
        assertEquals(2L, getResult.getId().longValue());
        assertEquals("Description", getResult.getDescription());
        assertFalse(getResult.getAvailable());
    }

    /**
     * Method under test: {@link ItemMapper#toItemWithBookingsResponseDto(Item, Booking, Booking, List)}
     */
    @Test
    void testToItemWithBookingsResponseDto() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user4);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user3);
        item1.setRequest(request1);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item1);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(1L);
        user7.setName("Name");

        Request request2 = new Request();
        request2.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);
        request2.setRequester(user7);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(user6);
        item2.setRequest(request2);

        Booking booking1 = new Booking();
        booking1.setBooker(user5);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(1L);
        booking1.setItem(item2);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        ArrayList<Comment> commentList = new ArrayList<>();
        ItemWithBookingsResponseDto actualToItemWithBookingsResponseDtoResult = ItemMapper
                .toItemWithBookingsResponseDto(item, booking, booking1, commentList);
        assertTrue(actualToItemWithBookingsResponseDtoResult.getAvailable());
        assertEquals("Name", actualToItemWithBookingsResponseDtoResult.getName());
        assertEquals(commentList, actualToItemWithBookingsResponseDtoResult.getComments());
        assertEquals(1L, actualToItemWithBookingsResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToItemWithBookingsResponseDtoResult.getDescription());
        ItemWithBookingsResponseDto.BookingDto lastBooking = actualToItemWithBookingsResponseDtoResult.getLastBooking();
        assertEquals(1L, lastBooking.getId().longValue());
        assertEquals(1L, lastBooking.getBookerId().longValue());
        ItemWithBookingsResponseDto.BookingDto nextBooking = actualToItemWithBookingsResponseDtoResult.getNextBooking();
        assertEquals(1L, nextBooking.getId().longValue());
        assertEquals(1L, nextBooking.getBookerId().longValue());
    }

    /**
     * Method under test: {@link ItemMapper#toItemWithBookingsResponseDto(Item, Booking, Booking, List)}
     */
    @Test
    void testToItemWithBookingsResponseDto2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user4);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user3);
        item1.setRequest(request1);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        ItemWithBookingsResponseDto actualToItemWithBookingsResponseDtoResult = ItemMapper
                .toItemWithBookingsResponseDto(item, null, null, commentList);
        assertTrue(actualToItemWithBookingsResponseDtoResult.getAvailable());
        assertEquals("Name", actualToItemWithBookingsResponseDtoResult.getName());
        assertEquals(1L, actualToItemWithBookingsResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something",
                actualToItemWithBookingsResponseDtoResult.getDescription());
        List<ItemWithBookingsResponseDto.CommentDto> comments = actualToItemWithBookingsResponseDtoResult.getComments();
        assertEquals(1, comments.size());
        ItemWithBookingsResponseDto.CommentDto getResult = comments.get(0);
        assertEquals("Name", getResult.getAuthorName());
        assertEquals("Text", getResult.getText());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("0001-01-01", getResult.getCreated().toLocalDate().toString());
    }

    /**
     * Method under test: {@link ItemMapper#toComment(CommentRequestDto, User, Item, LocalDateTime)}
     */
    @Test
    void testToComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");

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
        Comment actualToCommentResult = ItemMapper.toComment(commentRequestDto, user, item,
                LocalDateTime.of(1, 1, 1, 1, 1));
        assertSame(user, actualToCommentResult.getAuthor());
        assertEquals("Text", actualToCommentResult.getText());
        assertEquals("0001-01-01", actualToCommentResult.getTime().toLocalDate().toString());
        assertSame(item, actualToCommentResult.getItem());
    }

    /**
     * Method under test: {@link ItemMapper#toCommentResponseDto(Comment)}
     */
    @Test
    void testToCommentResponseDto() {
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
        CommentResponseDto actualToCommentResponseDtoResult = ItemMapper.toCommentResponseDto(comment);
        assertEquals("Name", actualToCommentResponseDtoResult.getAuthorName());
        assertEquals("Text", actualToCommentResponseDtoResult.getText());
        assertEquals(1L, actualToCommentResponseDtoResult.getId().longValue());
        assertEquals("0001-01-01", actualToCommentResponseDtoResult.getCreated().toLocalDate().toString());
    }
}

