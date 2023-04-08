package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.item.dto.request.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.response.DetailedItemResponseDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;

import java.util.Comparator;
import java.util.List;

@Service
public class ItemClient {
    private final WebClient client;

    public ItemClient(@Value("http://server:9090") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public ItemResponseDto addItem(CreateItemRequestDto itemDto, Long userId) {
        return client.post()
                .uri("/items")
                .bodyValue(itemDto)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemResponseDto.class)
                .block();
    }

    public ItemResponseDto updateItem(CreateItemRequestDto itemDto, Long itemId, Long userId) {
        return client.patch()
                .uri("/items/{id}", itemId)
                .bodyValue(itemDto)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemResponseDto.class)
                .block();
    }

    public DetailedItemResponseDto getItemById(Long itemId, Long userId) {
        return client.get()
                .uri("/items/{id}", itemId)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(DetailedItemResponseDto.class)
                .block();
    }

    public List<DetailedItemResponseDto> getItemsByOwnerId(Long userId, Integer from, Integer size) {
        return client.get()
                .uri("/items")
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .header("from", from.toString())
                .header("size", size.toString())
                .retrieve()
                .bodyToFlux(DetailedItemResponseDto.class)
                .sort(Comparator.comparingLong(DetailedItemResponseDto::getId))
                .collectList()
                .block();
    }

    public List<ItemResponseDto> searchItemsByNameOrDescription(String text, Integer from, Integer size, Long userId) {
        return client.get()
                .uri(builder -> builder.path("/items/search")
                        .queryParam("text", text)
                        .build())
                .header("from", from.toString())
                .header("size", size.toString())
                .header("X_SHARER_USER_ID", userId.toString())
                .retrieve()
                .bodyToFlux(ItemResponseDto.class)
                .collectList()
                .block();
    }
}