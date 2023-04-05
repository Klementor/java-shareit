package ru.practicum.shareit.item.client.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.dto.request.comment.CreateCommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.CommentResponseDto;

@Service
public class CommentClient {
    private final WebClient client;

    public CommentClient(@Value("http://server:9090") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public CommentResponseDto addComment(CreateCommentRequestDto comment, Long userId, Long itemId) {
        return client.post()
                .uri("/items/{id}/comment", itemId)
                .bodyValue(comment)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(CommentResponseDto.class)
                .block();
    }
}
