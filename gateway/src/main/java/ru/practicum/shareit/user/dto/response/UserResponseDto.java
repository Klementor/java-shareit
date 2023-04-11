package ru.practicum.shareit.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
}
