package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CommentRequestDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link CommentRequestDto}
     *   <li>{@link CommentRequestDto#setText(String)}
     *   <li>{@link CommentRequestDto#getText()}
     * </ul>
     */
    @Test
    void testConstructor() {
        CommentRequestDto actualCommentRequestDto = new CommentRequestDto();
        actualCommentRequestDto.setText("Text");
        assertEquals("Text", actualCommentRequestDto.getText());
    }
}

