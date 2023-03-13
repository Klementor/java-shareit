package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User user = userJpaRepository.save(UserMapper.fromUserRequestDto(userRequestDto));
        log.debug("Пользователь с id= {} добавлен", user.getId());
        return UserMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long userId) {
        checkUserExistsById(userId);
        User user = userJpaRepository.getReferenceById(userId);
        Optional.ofNullable(userRequestDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userRequestDto.getEmail()).ifPresent(user::setEmail);
        User updateUser = userJpaRepository.save(user);
        log.debug("Обновлен пользователь с id = {}", userId);
        return UserMapper.toUserResponseDto(updateUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        checkUserExistsById(userId);
        log.debug("Получен пользователь с id = {}", userId);
        return UserMapper.toUserResponseDto(userJpaRepository.getReferenceById(userId));
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        checkUserExistsById(userId);
        userJpaRepository.deleteById(userId);
        log.debug("Удален пользователь с id = {}", userId);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        log.debug("Найдены все пользователи");
        return UserMapper.toUserResponseDtoList(userJpaRepository.findAll());
    }

    private void checkUserExistsById(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }
}
