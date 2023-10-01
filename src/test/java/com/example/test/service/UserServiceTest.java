package com.example.test.service;

import com.example.test.domain.User;
import com.example.test.dto.DateRangeDto;
import com.example.test.dto.UpdateUserDto;
import com.example.test.exception.IncorrectDateException;
import com.example.test.repository.UserRepository;
import com.example.test.service.impl.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = "min.user.age=18")
@ContextConfiguration(classes = {UserServiceImpl.class})
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser(){
        User user = getUser();

        when(userRepository.save(user)).thenReturn(user);
        userService.createUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUserIncorrectBirthDate(){
        User user = getUser();
        user.setBirthDate(LocalDate.now());

        assertThrows(IncorrectDateException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUserThrowsEntityExistsException(){
        User user = getUser();
        user.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> userService.createUser(user));
        verify(userRepository).existsById(1L);
    }

    @Test
    void testUpdateUser1(){
        User user = getUser();

        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.updateUser(1L, user);
        verify(userRepository).existsById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser1ThrowsEntityNotFoundException(){
        User user = getUser();

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, user));
        verify(userRepository).existsById(1L);
    }

    @Test
    void testUpdateUser1ThrowsIncorrectDateException(){
        User user = getUser();
        user.setBirthDate(LocalDate.now());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        assertThrows(IncorrectDateException.class, () -> userService.updateUser(1L, user));
        verify(userRepository).existsById(1L);
    }

    @Test
    void testUpdateUser2WithoutNewData(){
        User user = getUser();
        UpdateUserDto updateUserDto = new UpdateUserDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(1L, updateUserDto);
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser2WithNewData(){
        User user = getUser();
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("newmail@mail.com");
        updateUserDto.setFirstName("New Name");
        updateUserDto.setLastName("New Name");
        updateUserDto.setBirthDate(LocalDate.EPOCH);
        updateUserDto.setAddress("New Address");
        updateUserDto.setPhoneNumber("+380000000000");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(1L, updateUserDto);
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser(){
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testGetUsersByBirthRange(){
        DateRangeDto dateRangeDto = new DateRangeDto();
        dateRangeDto.setFrom(LocalDate.MIN);
        dateRangeDto.setTo(LocalDate.MAX);

        when(userRepository.findByBirthDates(LocalDate.MIN, LocalDate.MAX)).thenReturn(List.of(getUser()));

        userService.getUsersByBirthRange(dateRangeDto);
        verify(userRepository).findByBirthDates(LocalDate.MIN, LocalDate.MAX);
    }

    @Test
    void testGetUsersByBirthRangeThrowsIncorrectDateException(){
        DateRangeDto dateRangeDto = new DateRangeDto();
        dateRangeDto.setFrom(LocalDate.MAX);
        dateRangeDto.setTo(LocalDate.MIN);

        assertThrows(IncorrectDateException.class, () -> userService.getUsersByBirthRange(dateRangeDto));
    }

    public static User getUser(){
        User user = new User();
        user.setEmail("user@mail.com");
        user.setFirstName("User");
        user.setLastName("User");
        user.setBirthDate(LocalDate.EPOCH);
        return user;
    }
}
