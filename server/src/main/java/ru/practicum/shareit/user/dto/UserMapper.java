package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User fromUserRequestDto(UserRequestDto userRequestDto) {
        User user = new User();

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());

        return user;
    }

    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());

        return userResponseDto;
    }

    public static List<UserResponseDto> toUserResponseDtoList(Collection<User> userList) {
        return userList.stream()
                .map(UserMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }
}
