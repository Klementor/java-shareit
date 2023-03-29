package ru.practicum.shareit.request.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

class RequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Request}
     *   <li>{@link Request#setDateTimeOfCreate(LocalDateTime)}
     *   <li>{@link Request#setDescription(String)}
     *   <li>{@link Request#setId(Long)}
     *   <li>{@link Request#setRequester(User)}
     *   <li>{@link Request#getDateTimeOfCreate()}
     *   <li>{@link Request#getDescription()}
     *   <li>{@link Request#getId()}
     *   <li>{@link Request#getRequester()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Request actualRequest = new Request();
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualRequest.setDateTimeOfCreate(ofResult);
        actualRequest.setDescription("The characteristics of someone or something");
        actualRequest.setId(1L);
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        actualRequest.setRequester(user);
        assertSame(ofResult, actualRequest.getDateTimeOfCreate());
        assertEquals("The characteristics of someone or something", actualRequest.getDescription());
        assertEquals(1L, actualRequest.getId().longValue());
        assertSame(user, actualRequest.getRequester());
    }
}

