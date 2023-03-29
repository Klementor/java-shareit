package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CommentResponseDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link CommentResponseDto}
     *   <li>{@link CommentResponseDto#setAuthorName(String)}
     *   <li>{@link CommentResponseDto#setCreated(LocalDateTime)}
     *   <li>{@link CommentResponseDto#setId(Long)}
     *   <li>{@link CommentResponseDto#setText(String)}
     *   <li>{@link CommentResponseDto#getAuthorName()}
     *   <li>{@link CommentResponseDto#getCreated()}
     *   <li>{@link CommentResponseDto#getId()}
     *   <li>{@link CommentResponseDto#getText()}
     * </ul>
     */
    @Test
    void testConstructor() {
        CommentResponseDto actualCommentResponseDto = new CommentResponseDto();
        actualCommentResponseDto.setAuthorName("JaneDoe");
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualCommentResponseDto.setCreated(ofResult);
        actualCommentResponseDto.setId(1L);
        actualCommentResponseDto.setText("Text");
        assertEquals("JaneDoe", actualCommentResponseDto.getAuthorName());
        assertSame(ofResult, actualCommentResponseDto.getCreated());
        assertEquals(1L, actualCommentResponseDto.getId().longValue());
        assertEquals("Text", actualCommentResponseDto.getText());
    }
}

