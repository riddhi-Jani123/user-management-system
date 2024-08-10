package com.user.management.service.impl;

import com.user.management.dto.UserDTO;
import com.user.management.entity.User;
import com.user.management.exception.UserNotFoundException;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link UserService} interface, providing business logic for user management.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of {@link UserDTO} representing all users.
     */
    @Override
    public List<UserDTO> getAllUser() {
        logger.info("Fetching all users");
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the {@link UserDTO} representing the user.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDTO getUser(long id) {
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    /**
     * Adds a new user to the repository.
     *
     * @param userDTO the {@link UserDTO} representing the new user to add.
     * @return the {@link UserDTO} representing the added user.
     */
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        logger.info("Adding new user: {}", userDTO.getName());
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update.
     * @param userDTO the {@link UserDTO} containing updated user data.
     * @return the {@link UserDTO} representing the updated user.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDTO updateUser(long id, UserDTO userDTO) {
        logger.info("Updating user with id: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete.
     * @throws UserNotFoundException if no user is found with the given ID.
     */
    @Override
    public void deleteUser(long id) {
        logger.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user the {@link User} entity to convert.
     * @return the {@link UserDTO} representing the user.
     */
    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().format(FORMATTER) : null);
        return userDTO;
    }

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO the {@link UserDTO} to convert.
     * @return the {@link User} entity.
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user, "createdAt", "id");
        return user;
    }
}