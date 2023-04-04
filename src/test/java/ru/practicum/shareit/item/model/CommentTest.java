package ru.practicum.shareit.item.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

class CommentTest {

    @Test
    void testConstructor() {
        Comment actualComment = new Comment();
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        actualComment.setAuthor(user);
        actualComment.setId(1L);
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
        actualComment.setItem(item);
        actualComment.setText("Text");
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualComment.setTime(ofResult);
        assertSame(user, actualComment.getAuthor());
        assertEquals(1L, actualComment.getId().longValue());
        assertSame(item, actualComment.getItem());
        assertEquals("Text", actualComment.getText());
        assertSame(ofResult, actualComment.getTime());
    }
}

