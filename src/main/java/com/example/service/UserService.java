package com.example.service;

import com.example.domain.User;
import com.example.dto.DateRangeDto;
import com.example.dto.UpdateUserDto;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void updateUser(Long id, User user);

    void updateUser(Long id, UpdateUserDto updateUserDto);

    void deleteUser(Long id);

    List<User> getUsersByBirthRange(DateRangeDto dateRangeDto);
}
