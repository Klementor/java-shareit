package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long userId);

    ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto, Long userId);

    ItemWithBookingsResponseDto getItemById(Long itemId, Long userId);

    List<ItemWithBookingsResponseDto> getUserItems(Long userId);

    List<ItemResponseDto> searchItemsByText(String text, Long userId);

    CommentResponseDto postComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}
