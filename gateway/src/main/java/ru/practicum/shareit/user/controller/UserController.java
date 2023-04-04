package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.request.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.validation.group.CreationGroup;
import ru.practicum.shareit.validation.group.UpdateGroup;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public UserResponseDto addUser(
            @RequestBody @Validated(CreationGroup.class) CreateUserRequestDto userDto) {
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(
            @RequestBody @Validated(UpdateGroup.class) CreateUserRequestDto userDto,
            @PathVariable Long userId) {
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(
            @PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @PathVariable Long userId) {
        userClient.deleteUserById(userId);
    }
}
