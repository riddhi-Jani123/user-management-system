package com.user.management.service.impl;

import com.user.management.dto.UserDTO;
import com.user.management.entity.User;
import com.user.management.exception.UserNotFoundException;
import com.user.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john.doe@example.com", "2024-08-10T00:25:45.238");
        User user = new User(1L, "John Doe", "john.doe@example.com", LocalDateTime.now(), null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.addUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUser() {
        User user = new User(1L, "John Doe", "john.doe@example.com", LocalDateTime.now(), null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(1L);

        assertNotNull(userDTO);
        assertEquals("John Doe", userDTO.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllUser() {
        // Arrange
        List<User> mockUserList = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", LocalDateTime.now(), null),
                new User(2L, "Jane Smith", "jane@example.com", LocalDateTime.now(), null)
        );

        when(userRepository.findAll()).thenReturn(mockUserList);
        List<UserDTO> result = userService.getAllUser();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("Jane Smith", result.get(1).getName());
        assertEquals("jane@example.com", result.get(1).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateUser() {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john.doe@example.com", "2024-08-10T00:25:45.238");
        User existingUser = new User(1L, "Jane Doe", "jane.doe@example.com", LocalDateTime.now(), null);
        User updatedUser = new User(1L, "John Doe", "john.doe@example.com", LocalDateTime.now(), null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDTO updatedUserDTO = userService.updateUser(1L, userDTO);

        assertNotNull(updatedUserDTO);
        assertEquals("John Doe", updatedUserDTO.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).existsById(1L);
    }
}
