package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.UserDTO;
import com.user.management.exception.UserNotFoundException;
import com.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john@example.com", "2024-08-09T12:00:00");
        when(userService.addUser(userDTO)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testCreateUserValidationError() throws Exception {
        UserDTO invalidUserDTO = new UserDTO();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Validation Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed for one or more fields"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", hasItems(
                        "name: Name is required",
                        "email: Email is required"
                )));
    }


    @Test
    public void testGetUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john@example.com", "2024-08-09T12:00:00");
        when(userService.getUser(1L)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "Jane Doe", "jane@example.com", "2024-08-09T12:00:00");
        when(userService.updateUser(1L, userDTO)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john@example.com", "2024-08-09T12:00:00");
        when(userService.getAllUser()).thenReturn(Collections.singletonList(userDTO));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response Content: " + content);

        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    public void testGetUserNotFoundException() throws Exception {
        when(userService.getUser(1L)).thenThrow(new UserNotFoundException("User not found with id: 1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("User Not Found Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found with id: 1"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        Long userId = 1L;
        UserDTO updateUserDTO = new UserDTO(1l,"Updated Name", "updated@example.com", "2024-08-09T12:00:00");

        when(userService.updateUser(userId, updateUserDTO)).thenThrow(new UserNotFoundException("User not found with id: " + userId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("User Not Found Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found with id: " + userId));
    }


}
