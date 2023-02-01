package ru.practicum.shareit.user.service.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private Long id = 1L;
    @Getter
    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User user = UserMapper.fromUserRequestDto(userRequestDto);
        checkUniqueEmail(user.getEmail());
        user.setId(id);
        id++;
        userMap.put(user.getId(), user);
        log.debug("Пользователь с id= {} добавлен", user.getId());
        return UserMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long userId) {
        checkUserExistsById(userId);
        checkUniqueEmail(userRequestDto.getEmail());
        User user = userMap.get(userId);
        Optional.ofNullable(userRequestDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userRequestDto.getEmail()).ifPresent(user::setEmail);
        userMap.put(userId, user);
        return UserMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        checkUserExistsById(userId);
        return UserMapper.toUserResponseDto(userMap.get(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        checkUserExistsById(userId);
        userMap.remove(userId);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return UserMapper.toUserResponseDtoList(userMap.values());
    }

    private void checkUserExistsById(Long userId) {
        if (!userMap.containsKey(userId)) {
            throw new RuntimeException("Пользователя с таким id не существует");
        }
    }

    private void checkUniqueEmail(String email) {
        for (User user1 : userMap.values()) {
            if (user1.getEmail().equals(email)) {
                throw new RuntimeException("Пользователь с данной почтой уже зарегестрирован");
            }
        }
    }
}
