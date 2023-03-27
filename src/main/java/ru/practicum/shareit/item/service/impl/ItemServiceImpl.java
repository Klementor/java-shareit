package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Booking.Status.REJECTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final BookingJpaRepository bookingJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final ItemJpaRepository itemJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final ItemRequestJpaRepository itemRequestJpaRepository;

    @Override
    public ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long userId) {
        Item item = ItemMapper.toItem(itemRequestDto);
        if (itemRequestDto.getRequestId() != null) {
            item.setRequest(itemRequestJpaRepository.getReferenceById(itemRequestDto.getRequestId()));
        }
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
    public ItemWithBookingsResponseDto getItemById(Long itemId, Long userId) {
        checkItemExistsById(itemId);
        log.debug("Получен предмет с id = {} пользователем с id = {}", itemId, userId);
        Item item = itemJpaRepository.getReferenceById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = bookingJpaRepository
                    .findFirstByItemIdAndEndIsBeforeOrderByEndDesc(item.getId(), LocalDateTime.now().plusSeconds(5))
                    .orElse(null);
            if (lastBooking != null && lastBooking.getStatus() == REJECTED) {
                lastBooking = null;
            }
            Booking nextBooking = bookingJpaRepository
                    .findFirstByItemIdAndStartIsAfterOrderByStart(item.getId(), LocalDateTime.now())
                    .orElse(null);
            if (nextBooking != null && nextBooking.getStatus() == REJECTED) {
                nextBooking = null;
            }
            return ItemMapper.toItemWithBookingsResponseDto(item,
                    lastBooking,
                    nextBooking,
                    commentJpaRepository.findAllByItemId(itemId));
        }
        return ItemMapper.toItemWithBookingsResponseDto(item,
                null,
                null,
                commentJpaRepository.findAllByItemId(itemId));
    }

    @Override
    public List<ItemWithBookingsResponseDto> getUserItems(Long userId) {
        checkUserExistsById(userId);
        log.debug("Получение всех предметов пользователя с id = {}", userId);
        List<Item> items = itemJpaRepository.findAllByOwnerId(userId);
        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> lastBookings = bookingJpaRepository
                .findFirstByItemIdInAndEndIsBeforeOrderByEndDesc(itemIds, LocalDateTime.now());
        List<Booking> nextBookings = bookingJpaRepository
                .findFirstByItemIdInAndStartIsAfterOrderByStart(itemIds, LocalDateTime.now());
        Map<Long, Booking> lastBookingMap = lastBookings.stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));
        Map<Long, Booking> nextBookingMap = nextBookings.stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity()));
        Map<Long, List<Comment>> commentMap = commentJpaRepository
                .findAllByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        List<ItemWithBookingsResponseDto> itemWithBookingsResponseDtoList = new ArrayList<>();
        for (Item item : items) {
            itemWithBookingsResponseDtoList.add(ItemMapper.toItemWithBookingsResponseDto(item,
                    lastBookingMap.get(item.getId()),
                    nextBookingMap.get(item.getId()),
                    commentMap.getOrDefault(item.getId(), Collections.emptyList())));
        }

        return itemWithBookingsResponseDtoList;
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

    @Override
    public CommentResponseDto postComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        if (!itemJpaRepository.existsById(itemId)) {
            throw new NotFoundException("Вещи с указанным id не существует");
        }
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с указанным id не существует");
        }
        LocalDateTime time = LocalDateTime.now();
        if (!bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore(userId, itemId, time)) {
            throw new BadRequestException("Бронирование вещи с указанным id пользователем с указанным id не найдено");
        }
        Comment comment = ItemMapper.toComment(commentRequestDto,
                userJpaRepository.getReferenceById(userId),
                itemJpaRepository.getReferenceById(itemId),
                time);
        return ItemMapper.toCommentResponseDto(commentJpaRepository.save(comment));
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
