package ru.practicum.shareit.request.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.group.CreationGroup;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class RequestItemRequestDto {
    @NotNull(groups = {CreationGroup.class})
    private String description;
}
