package com.example.service;

import com.example.domain.User;
import com.example.dto.DateRangeDto;
import com.example.dto.UpdateUserDto;
import com.example.service.impl.UserServiceImpl;
import com.example.exception.IncorrectDateException;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.ModelUtils.getUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "minimalAge", 18);
    }

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
    void testFullUpdateUser(){
        User user = getUser();

        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.updateUser(1L, user);
        verify(userRepository).existsById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testFullUpdateUserThrowsEntityNotFoundException(){
        User user = getUser();

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, user));
        verify(userRepository).existsById(1L);
    }

    @Test
    void testFullUpdateUserThrowsIncorrectDateException(){
        User user = getUser();
        user.setBirthDate(LocalDate.now());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        assertThrows(IncorrectDateException.class, () -> userService.updateUser(1L, user));
        verify(userRepository).existsById(1L);
    }

    @Test
    void testPartialUpdateUserWithoutNewData(){
        User user = getUser();
        UpdateUserDto updateUserDto = new UpdateUserDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(1L, updateUserDto);
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testPartialUpdateUserWithNewData(){
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
        DateRangeDto dateRangeDto = new DateRangeDto(LocalDate.MIN, LocalDate.MAX);

        when(userRepository.findByBirthDates(LocalDate.MIN, LocalDate.MAX)).thenReturn(List.of(getUser()));

        userService.getUsersByBirthRange(dateRangeDto);
        verify(userRepository).findByBirthDates(LocalDate.MIN, LocalDate.MAX);
    }

    @Test
    void testGetUsersByBirthRangeThrowsIncorrectDateException(){
        DateRangeDto dateRangeDto = new DateRangeDto(LocalDate.MAX, LocalDate.MIN);

        assertThrows(IncorrectDateException.class, () -> userService.getUsersByBirthRange(dateRangeDto));
    }
}
