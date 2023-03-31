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

    @Test
    void testToItemResponseDto() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);

        Request request = createRequest(user1);

        Item item = createItem(user, request);
        ItemResponseDto actualToItemResponseDtoResult = ItemMapper.toItemResponseDto(item);
        assertTrue(actualToItemResponseDtoResult.getAvailable());
        assertEquals(1L, actualToItemResponseDtoResult.getRequestId().longValue());
        assertEquals("Name", actualToItemResponseDtoResult.getName());
        assertEquals(1L, actualToItemResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemResponseDtoResult.getDescription());
    }

    @Test
    void testToItemResponseDtoSecond() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);

        Item item = createItem(user, null);
        ItemResponseDto actualToItemResponseDtoResult = ItemMapper.toItemResponseDto(item);
        assertTrue(actualToItemResponseDtoResult.getAvailable());
        assertEquals("Name", actualToItemResponseDtoResult.getName());
        assertEquals(1L, actualToItemResponseDtoResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemResponseDtoResult.getDescription());
    }

    @Test
    void testToItem() {
        Item actualToItemResult = ItemMapper.toItem(new ItemRequestDto());
        assertNull(actualToItemResult.getAvailable());
        assertNull(actualToItemResult.getName());
        assertNull(actualToItemResult.getId());
        assertNull(actualToItemResult.getDescription());
    }

    @Test
    void testFromItemListToItemResponseDtoList() {
        assertTrue(ItemMapper.fromItemListToItemResponseDtoList(new ArrayList<>()).isEmpty());
    }

    @Test
    void testFromItemListToItemResponseDtoListSecond() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);

        Request request = createRequest(user1);

        Item item = createItem(user, request);

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

    @Test
    void testFromItemListToItemResponseDtoListThird() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);
        User user2 = listUsers.get(2);

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(4L);
        user3.setName("42");

        Request request1 = createRequest(user3);

        Item item1 = createItem(user2, request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        List<ItemResponseDto> actualFromItemListToItemResponseDtoListResult = ItemMapper
                .fromItemListToItemResponseDtoList(itemList);
        assertEquals(2, actualFromItemListToItemResponseDtoListResult.size());
        ItemResponseDto getResult = actualFromItemListToItemResponseDtoListResult.get(0);
        assertEquals(1L, getResult.getRequestId().longValue());
        ItemResponseDto getResult1 = actualFromItemListToItemResponseDtoListResult.get(1);
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("Name", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        assertTrue(getResult1.getAvailable());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
    }

    @Test
    void testToItemWithBookingsResponseDto() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);
        User user2 = listUsers.get(2);

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(4L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(5L);
        user4.setName("Name");

        Request request1 = createRequest(user4);

        Item item1 = createItem(user3, request1);

        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item1);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(6L);
        user5.setName("Name");

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(7L);
        user6.setName("Name");

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(8L);
        user7.setName("Name");

        Request request2 = createRequest(user7);

        Item item2 = createItem(user6, request2);

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
        ItemWithBookingsResponseDto.BookingDto nextBooking = actualToItemWithBookingsResponseDtoResult.getNextBooking();
    }

    @Test
    void testToItemWithBookingsResponseDtoSecond() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);
        User user2 = listUsers.get(2);

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        Request request1 = createRequest(user4);

        Item item1 = createItem(user3, request1);

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

    @Test
    void testToComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");

        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);
        User user2 = listUsers.get(2);

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Comment actualToCommentResult = ItemMapper.toComment(commentRequestDto, user, item,
                LocalDateTime.of(1, 1, 1, 1, 1));
        assertSame(user, actualToCommentResult.getAuthor());
        assertEquals("Text", actualToCommentResult.getText());
        assertEquals("0001-01-01", actualToCommentResult.getTime().toLocalDate().toString());
        assertSame(item, actualToCommentResult.getItem());
    }

    @Test
    void testToCommentResponseDto() {
        List<User> listUsers = createUsers();
        User user = listUsers.get(0);
        User user1 = listUsers.get(1);
        User user2 = listUsers.get(2);

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

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

    private List<User> createUsers() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(2L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(3L);
        user2.setName("Name");
        List<User> listUsers = new ArrayList<>();
        listUsers.add(user);
        listUsers.add(user1);
        listUsers.add(user2);
        return listUsers;
    }

    private Item createItem(User user, Request request) {
        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);
        return item;
    }

    private Request createRequest(User user) {
        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        return request;
    }
}

