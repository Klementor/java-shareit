package ru.practicum.shareit.request.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestMapper {
    public static Request toRequest(RequestDto itemRequestDto, User user) {
        Request request = new Request();
        request.setDescription(itemRequestDto.getDescription());
        request.setRequester(user);
        return request;
    }

    public static RequestResponseDto toResponse(Request request, List<Item> items) {
        RequestResponseDto response = new RequestResponseDto();
        response.setId(request.getId());
        response.setDescription(request.getDescription());
        response.setCreated(request.getDateTimeOfCreate());
        if (items != null) {
            var itemDtoList = new ArrayList<RequestResponseDto.ItemDto>();
            for (Item item : items) {
                itemDtoList.add(RequestResponseDto.ItemDto.toItemDtoFromItem(item));
            }
            response.setItems(itemDtoList);
        }
        return response;
    }
}
