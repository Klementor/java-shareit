package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final String header = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public RequestResponseDto postNewItemRequest(@RequestBody @Valid RequestDto itemRequestDto,
                                                 @RequestHeader(header) Long userId) {
        return requestService.postNewItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<RequestResponseDto> getItemRequestsByRequesterId(@RequestHeader(header) Long userId) {
        return requestService.getItemRequestByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<RequestResponseDto> getAllItemRequests(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestHeader(header) Long userId) {
        return requestService.getAllItemRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public RequestResponseDto getItemRequestById(@PathVariable Long requestId,
                                                 @RequestHeader(header) Long userId) {
        return requestService.getItemRequestById(requestId, userId);
    }
}
