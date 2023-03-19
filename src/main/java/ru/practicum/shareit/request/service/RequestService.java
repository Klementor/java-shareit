package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;

import java.util.List;

public interface RequestService {
    RequestResponseDto postNewItemRequest(RequestDto itemRequestDto, Long userId);

    List<RequestResponseDto> getItemRequestByRequesterId(Long userId);

    List<RequestResponseDto> getAllItemRequests(Integer from, Integer size, Long requesterId);

    RequestResponseDto getItemRequestById(Long requestId, Long userId);
}
