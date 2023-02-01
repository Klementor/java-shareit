package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> itemMap = new HashMap<>();

    private final UserService userService;

    private Long id = 1L;

    @Override
    public ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long userId) {
        Item item = ItemMapper.toItem(itemRequestDto);
        checkUserExistsById(userId);
        item.setOwner(userService.getUserMap().get(userId));
        item.setId(id);
        itemMap.put(item.getId(), item);
        id++;
        log.debug("Добавлен новый предмет пользователем с id = {}", userId);
        return ItemMapper.toItemResponseDto(item);
    }

    @Override
    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto, Long userId) {
        checkItemExistsById(itemId);
        checkUserExistsById(userId);
        checkItemOwner(itemId, userId);
        Item item = itemMap.get(itemId);
        Optional.ofNullable(itemRequestDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemRequestDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemRequestDto.getAvailable()).ifPresent(item::setAvailable);
        itemMap.put(itemId, item);
        log.debug("Обновлен предмет с id = {} пользователем с id = {}", itemId, userId);
        return ItemMapper.toItemResponseDto(item);
    }

    @Override
    public ItemResponseDto getItemById(Long itemId, Long userId) {
        checkItemExistsById(itemId);
        log.debug("Получен предмет с id = {} пользователем с id = {}", itemId, userId);
        return ItemMapper.toItemResponseDto(itemMap.get(itemId));
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        checkUserExistsById(userId);
        List<Item> items = itemMap.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .collect(Collectors.toList());
        log.debug("Получение всех предметов пользователя с id = {}", userId);
        return ItemMapper.fromItemListToItemResponseDtoList(items);
    }

    @Override
    public List<ItemResponseDto> searchItemsByText(String text, Long userId) {
        if (text == null || text.isEmpty()) {
            log.debug("Текст поиска пустой или равен null, возвращен пустой список");
            return Collections.emptyList();
        }
        text = text.toLowerCase();
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (!item.getAvailable()) {
                continue;
            }
            boolean nameContains = item.getName().toLowerCase().contains(text);
            boolean descriptionContains = item.getDescription().toLowerCase().contains(text);
            if (nameContains || descriptionContains) {
                itemList.add(item);
            }
        }
        log.debug("Найдены все предметы с подстрокой = {}", text);
        return ItemMapper.fromItemListToItemResponseDtoList(itemList);
    }

    private void checkUserExistsById(Long userId) {
        if (!userService.getUserMap().containsKey(userId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
    }

    private void checkItemExistsById(Long itemId) {
        if (!itemMap.containsKey(itemId)) {
            throw new ItemNotFoundException("Предмета с таким id не существует");
        }
    }

    private void checkItemOwner(Long itemId, Long userId) {
        if (itemMap.get(itemId).getOwner() == null || !itemMap.get(itemId).getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("У данной вещи другой хозяиин");
        }
    }
}
