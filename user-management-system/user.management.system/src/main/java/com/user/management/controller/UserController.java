package com.user.management.controller;

import com.user.management.dto.UserDTO;
import com.user.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Create a new user
     * @param userDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO user  = userService.addUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by ID.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        UserDTO user  = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates an existing user by ID.
     * @param userDTO
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long id){
        UserDTO user  = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * Deletes a user by ID.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all users.
     * @return
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(){
        List<UserDTO> user  = userService.getAllUser();
        return ResponseEntity.ok(user);
    }
}
