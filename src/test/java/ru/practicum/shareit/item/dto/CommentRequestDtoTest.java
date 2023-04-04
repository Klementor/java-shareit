package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CommentRequestDtoTest {

    @Test
    void testConstructor() {
        CommentRequestDto actualCommentRequestDto = new CommentRequestDto();
        actualCommentRequestDto.setText("Text");
        assertEquals("Text", actualCommentRequestDto.getText());
    }
}

