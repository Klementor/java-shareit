package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.validation.annotation.group.ForCreate;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto createItem(@RequestBody @Validated(ForCreate.class) ItemRequestDto itemRequestDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение предмета по id = {}", userId);
        return itemService.addItem(itemRequestDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@PathVariable Long itemId,
                                      @RequestBody ItemRequestDto itemRequestDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на изменение данных о предмете с id = {}", itemId);
        return itemService.updateItem(itemId, itemRequestDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsResponseDto getItemById(@PathVariable Long itemId,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение предмета по id = {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemWithBookingsResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение предметов пользователя по id = {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByText(@RequestParam String text,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на поиск предметов по строке = {}", text);
        return itemService.searchItemsByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId,
                                          @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemService.postComment(userId, itemId, commentRequestDto);
    }
}