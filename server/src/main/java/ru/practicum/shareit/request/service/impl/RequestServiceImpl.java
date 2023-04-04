package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserJpaRepository userJpaRepository;

    private final ItemJpaRepository itemJpaRepository;
    private final ItemRequestJpaRepository itemRequestJpaRepository;


    @Override
    public RequestResponseDto postNewItemRequest(RequestDto itemRequestDto, Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        User user = userJpaRepository.getReferenceById(userId);
        Request request = RequestMapper.toRequest(itemRequestDto, user);
        Request savedRequest = itemRequestJpaRepository.save(request);
        return RequestMapper.toResponse(savedRequest, null);
    }

    @Override
    public List<RequestResponseDto> getItemRequestByRequesterId(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        List<Request> requests = itemRequestJpaRepository.findAllByRequesterId(userId,
                Sort.by("dateTimeOfCreate").descending());
        List<List<Item>> items = requests.stream()
                .map(r -> itemJpaRepository
                        .findAllByRequestId(r.getId())).collect(Collectors.toList());
        List<RequestResponseDto> listResponseItems = new ArrayList<>();
        for (Request request : requests) {
            for (List<Item> itemList : items) {
                listResponseItems.add(RequestMapper.toResponse(request, itemList));
                break;
            }
            break;
        }
        return listResponseItems;
    }

    @Override
    public List<RequestResponseDto> getAllItemRequests(Integer from, Integer size, Long requesterId) {
        if (requesterId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }
        List<Request> itemRequests = itemRequestJpaRepository
                .findOtherUserItems(requesterId, PageRequest.of(from / size, size,
                        Sort.by("dateTimeOfCreate").descending()));
        List<RequestResponseDto> requestResponseDtos = new ArrayList<>();
        for (Request request : itemRequests) {
            List<Item> items = itemJpaRepository.findAllByRequestId(request.getId());
            requestResponseDtos.add(RequestMapper.toResponse(request, items));
        }
        return requestResponseDtos;
    }

    @Override
    public RequestResponseDto getItemRequestById(Long requestId, Long userId) {
        if (userId == null) {
            throw new BadRequestException("В запросе не был передан заголовок X-Sharer-User-Id");
        }
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        if (!itemRequestJpaRepository.existsById(requestId)) {
            throw new NotFoundException("Запроса с таким id не существует");
        }
        Request request = itemRequestJpaRepository.getReferenceById(requestId);
        List<Item> items = itemJpaRepository.findAllByRequestId(requestId);
        return RequestMapper.toResponse(request, items);
    }
}
