package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
