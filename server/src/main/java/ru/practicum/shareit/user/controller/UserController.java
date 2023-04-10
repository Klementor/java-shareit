package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto addUser(@RequestBody UserRequestDto userRequestDto) {
        log.info("Запрос на добавление пользователя");
        return userService.addUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@RequestBody UserRequestDto userRequestDto,
                                      @PathVariable Long userId) {
        log.info("Запрос на изменение пользователя");
        return userService.updateUser(userRequestDto, userId);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable Long userId) {
        log.info("Запрос на получение пользователя по id = {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Удаление пользователя по id = {}", userId);
        userService.deleteUserById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.getUsers();
    }
}
