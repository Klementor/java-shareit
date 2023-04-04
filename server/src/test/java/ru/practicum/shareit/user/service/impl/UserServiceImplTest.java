package ru.practicum.shareit.user.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    void testAddUser() {
        User user = createUser();
        when(userJpaRepository.save((User) any())).thenReturn(user);
        UserResponseDto actualAddUserResult = userServiceImpl.addUser(new UserRequestDto("Name", "jane.doe@example.org"));
        assertEquals("jane.doe@example.org", actualAddUserResult.getEmail());
        assertEquals("Name", actualAddUserResult.getName());
        assertEquals(1L, actualAddUserResult.getId().longValue());
        verify(userJpaRepository).save((User) any());
    }

    @Test
    void testAddUserNotFoundException() {
        when(userJpaRepository.save((User) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class,
                () -> userServiceImpl.addUser(new UserRequestDto("Name", "jane.doe@example.org")));
        verify(userJpaRepository).save((User) any());
    }

    @Test
    void testUpdateUser() {
        User user = createUser();

        User user1 = createUser();
        when(userJpaRepository.save((User) any())).thenReturn(user1);
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        UserResponseDto actualUpdateUserResult = userServiceImpl
                .updateUser(new UserRequestDto("Name", "jane.doe@example.org"), 1L);
        assertEquals("jane.doe@example.org", actualUpdateUserResult.getEmail());
        assertEquals("Name", actualUpdateUserResult.getName());
        assertEquals(1L, actualUpdateUserResult.getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(userJpaRepository).save((User) any());
    }

    @Test
    void testUpdateUserNotFoundException() {
        User user = createUser();
        when(userJpaRepository.save((User) any())).thenThrow(new NotFoundException("An error occurred"));
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class,
                () -> userServiceImpl.updateUser(new UserRequestDto("Name", "jane.doe@example.org"), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(userJpaRepository).save((User) any());
    }

    @Test
    void testUpdateUserNotFoundEx() {
        User user = createUser();

        User user1 = createUser();
        when(userJpaRepository.save((User) any())).thenReturn(user1);
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class,
                () -> userServiceImpl.updateUser(new UserRequestDto("Name", "jane.doe@example.org"), 1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testGetUserById() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        UserResponseDto actualUserById = userServiceImpl.getUserById(1L);
        assertEquals("jane.doe@example.org", actualUserById.getEmail());
        assertEquals("Name", actualUserById.getName());
        assertEquals(1L, actualUserById.getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void testGetUserByIdNotFoundException() {
        when(userJpaRepository.getReferenceById((Long) any())).thenThrow(new NotFoundException("An error occurred"));
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void testGetUserByIdNotFoundEx() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userJpaRepository).deleteById((Long) any());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        userServiceImpl.deleteUserById(1L);
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteUserByIdNotFoundException() {
        doThrow(new NotFoundException("An error occurred")).when(userJpaRepository).deleteById((Long) any());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> userServiceImpl.deleteUserById(1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteUserByIdNotFoundEx() {
        doNothing().when(userJpaRepository).deleteById((Long) any());
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userServiceImpl.deleteUserById(1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testGetUsers() {
        when(userJpaRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(userServiceImpl.getUsers().isEmpty());
        verify(userJpaRepository).findAll();
    }

    @Test
    void testGetUser() {
        User user = createUser();

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userJpaRepository.findAll()).thenReturn(userList);
        List<UserResponseDto> actualUsers = userServiceImpl.getUsers();
        assertEquals(1, actualUsers.size());
        UserResponseDto getResult = actualUsers.get(0);
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        verify(userJpaRepository).findAll();
    }

    @Test
    void testGetUsersForTwoUsers() {
        User user = createUser();

        User user1 = new User();
        user1.setEmail("john.smith@example.org");
        user1.setId(2L);
        user1.setName("Name");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);
        when(userJpaRepository.findAll()).thenReturn(userList);
        List<UserResponseDto> actualUsers = userServiceImpl.getUsers();
        assertEquals(2, actualUsers.size());
        UserResponseDto getResult = actualUsers.get(0);
        assertEquals("Name", getResult.getName());
        UserResponseDto getResult1 = actualUsers.get(1);
        assertEquals("Name", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("jane.doe@example.org", getResult1.getEmail());
        assertEquals(2L, getResult.getId().longValue());
        assertEquals("john.smith@example.org", getResult.getEmail());
        verify(userJpaRepository).findAll();
    }

    @Test
    void testGetUsersNotFoundEx() {
        when(userJpaRepository.findAll()).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> userServiceImpl.getUsers());
        verify(userJpaRepository).findAll();
    }

    private User createUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        return user;
    }
}

