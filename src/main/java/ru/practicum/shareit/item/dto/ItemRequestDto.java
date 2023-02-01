package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.annotation.group.ForCreate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestDto {
    private Long id;

    @NotEmpty(groups = {ForCreate.class})
    private String name;

    @NotEmpty(groups = {ForCreate.class})
    private String description;

    @NotNull(groups = {ForCreate.class})
    private Boolean available;

}
