package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    private final UserJpaRepository userJpaRepository;

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long userId) {
        Item item = ItemMapper.toItem(itemRequestDto);
        checkUserExistsById(userId);
        item.setOwner(userJpaRepository.getReferenceById(userId));

        log.debug("Добавлен новый предмет пользователем с id = {}", userId);
        return ItemMapper.toItemResponseDto(itemJpaRepository.save(item));
    }

    @Override
    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto, Long userId) {
        checkItemExistsById(itemId);
        checkUserExistsById(userId);
        checkItemOwner(itemId, userId);

        Item item = itemJpaRepository.getReferenceById(itemId);
        Optional.ofNullable(itemRequestDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemRequestDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemRequestDto.getAvailable()).ifPresent(item::setAvailable);

        log.debug("Обновлен предмет с id = {} пользователем с id = {}", itemId, userId);
        return ItemMapper.toItemResponseDto(itemJpaRepository.save(item));
    }

    @Override
    public ItemResponseDto getItemById(Long itemId, Long userId) {
        checkItemExistsById(itemId);
        log.debug("Получен предмет с id = {} пользователем с id = {}", itemId, userId);
        return ItemMapper.toItemResponseDto(itemJpaRepository.getReferenceById(itemId));
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        checkUserExistsById(userId);
        log.debug("Получение всех предметов пользователя с id = {}", userId);
        return ItemMapper.fromItemListToItemResponseDtoList(itemJpaRepository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemResponseDto> searchItemsByText(String text, Long userId) {
        if (StringUtils.isBlank(text)) {
            log.debug("Текст поиска пустой или равен null, возвращен пустой список");
            return Collections.emptyList();
        }
        List<Item> items = itemJpaRepository.findAllByText(text);
        log.debug("Найдены все предметы с подстрокой = {}", text);
        return ItemMapper.fromItemListToItemResponseDtoList(items);
    }

    private void checkUserExistsById(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
    }

    private void checkItemExistsById(Long itemId) {
        if (!itemJpaRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Предмета с таким id не существует");
        }
    }

    private void checkItemOwner(Long itemId, Long userId) {
        Item item = itemJpaRepository.getReferenceById(itemId);
        if (item.getOwner() == null || !item.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("У данной вещи другой хозяин");
        }
    }
}
