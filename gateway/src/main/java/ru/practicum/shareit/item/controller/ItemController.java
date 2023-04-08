package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.client.comment.CommentClient;
import ru.practicum.shareit.item.dto.request.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.request.comment.CreateCommentRequestDto;
import ru.practicum.shareit.item.dto.response.DetailedItemResponseDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;
import ru.practicum.shareit.item.dto.response.comment.CommentResponseDto;
import ru.practicum.shareit.validation.group.CreationGroup;
import ru.practicum.shareit.validation.group.UpdateGroup;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private final CommentClient commentClient;

    @PostMapping
    public ItemResponseDto addItem(
            @RequestBody @Validated(CreationGroup.class) CreateItemRequestDto itemDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long ownerId
    ) {
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(
            @RequestBody @Validated(UpdateGroup.class) CreateItemRequestDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public DetailedItemResponseDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public List<DetailedItemResponseDto> getItemsByOwnerId(
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return itemClient.getItemsByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByNameOrDescription(
            @RequestParam String text,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemClient.searchItemsByNameOrDescription(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(
            @RequestBody @Validated(CreationGroup.class) CreateCommentRequestDto comment,
            @PathVariable Long itemId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return commentClient.addComment(comment, userId, itemId);
    }
}
