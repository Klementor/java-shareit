package ru.practicum.shareit.item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.annotation.NotEmptyIfNotNull;
import ru.practicum.shareit.validation.group.CreationGroup;
import ru.practicum.shareit.validation.group.UpdateGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class CreateItemRequestDto {
    @NotEmpty(groups = {CreationGroup.class})
    @NotEmptyIfNotNull(groups = {UpdateGroup.class})
    private String name;

    @NotEmpty(groups = {CreationGroup.class})
    @NotEmptyIfNotNull(groups = {UpdateGroup.class})
    private String description;

    @NotNull(groups = {CreationGroup.class})
    private Boolean available;

    private Long requestId;
}
