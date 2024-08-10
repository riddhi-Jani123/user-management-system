package com.user.management.service;

import com.user.management.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUser();
    UserDTO getUser(long id);
    UserDTO addUser(UserDTO user);
    UserDTO updateUser(long id, UserDTO user);
    void deleteUser(long id);


}
