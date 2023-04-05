package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.user.dto.request.CreateUserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;

import java.util.List;

@Service
public class UserClient {
    private final WebClient client;

    public UserClient(@Value("http://server:9090") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public UserResponseDto addUser(CreateUserRequestDto userDto) {
        return client.post()
                .uri("/users")
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block();
    }

    public UserResponseDto updateUser(CreateUserRequestDto userDto, Long userId) {
        return client.patch()
                .uri("/users/{id}", userId)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block();
    }

    public UserResponseDto getUserById(Long userId) {
        return client.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block();
    }


    public List<UserResponseDto> getAllUsers() {
        return client.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(UserResponseDto.class)
                .collectList()
                .block();
    }

    public void deleteUserById(Long userId) {
        client.delete()
                .uri("/users/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
