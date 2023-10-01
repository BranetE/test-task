package com.example.test.service;

import com.example.test.domain.User;
import com.example.test.dto.DateRangeDto;
import com.example.test.dto.UpdateUserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {
    void createUser(User user);

    void updateUser(Long id, User user);

    void updateUser(Long id, UpdateUserDto updateUserDto);

    void deleteUser(Long id);

    List<User> getUsersByBirthRange(DateRangeDto dateRangeDto);
}
