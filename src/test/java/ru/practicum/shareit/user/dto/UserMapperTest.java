package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {

    @Test
    void testFromUserRequestDto() {
        User actualFromUserRequestDtoResult = UserMapper
                .fromUserRequestDto(new UserRequestDto("Name", "jane.doe@example.org"));
        assertEquals("jane.doe@example.org", actualFromUserRequestDtoResult.getEmail());
        assertEquals("Name", actualFromUserRequestDtoResult.getName());
    }

    @Test
    void testToUserResponseDto() {
        User user = createUser();
        UserResponseDto actualToUserResponseDtoResult = UserMapper.toUserResponseDto(user);
        assertEquals("jane.doe@example.org", actualToUserResponseDtoResult.getEmail());
        assertEquals("Name", actualToUserResponseDtoResult.getName());
        assertEquals(1L, actualToUserResponseDtoResult.getId().longValue());
    }

    @Test
    void testToUserResponseDtoList() {
        assertTrue(UserMapper.toUserResponseDtoList(new ArrayList<>()).isEmpty());
    }

    @Test
    void testToUserResponseDtoListSecond() {
        User user = createUser();

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        List<UserResponseDto> actualToUserResponseDtoListResult = UserMapper.toUserResponseDtoList(userList);
        assertEquals(1, actualToUserResponseDtoListResult.size());
        UserResponseDto getResult = actualToUserResponseDtoListResult.get(0);
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
    }

    @Test
    void testToUserResponseDtoListThird() {
        User user = createUser();

        User user1 = new User();
        user1.setEmail("john.smith@example.org");
        user1.setId(2L);
        user1.setName("42");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);
        List<UserResponseDto> actualToUserResponseDtoListResult = UserMapper.toUserResponseDtoList(userList);
        assertEquals(2, actualToUserResponseDtoListResult.size());
        UserResponseDto getResult = actualToUserResponseDtoListResult.get(0);
        assertEquals("42", getResult.getName());
        UserResponseDto getResult1 = actualToUserResponseDtoListResult.get(1);
        assertEquals("Name", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("jane.doe@example.org", getResult1.getEmail());
        assertEquals(2L, getResult.getId().longValue());
        assertEquals("john.smith@example.org", getResult.getEmail());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        return user;
    }
}

