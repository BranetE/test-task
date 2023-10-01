package com.example.controller;

import com.example.contoller.UserController;
import com.example.domain.User;
import com.example.dto.DateRangeDto;
import com.example.dto.UpdateUserDto;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.ModelUtils.getUpdateUserDto;
import static com.example.ModelUtils.getUser;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void testCreateUser() {
        User user = getUser();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(userService).createUser(any(User.class));
    }

    @Test
    @SneakyThrows
    void testCreateUserThrowsValidationException() {
        User user = getUser();
        user.setEmail("Not correct email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void testFullUpdateUser() {
        User user = getUser();

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    @SneakyThrows
    void testFullUpdateUserThrowsValidationException() {
        User user = getUser();
        user.setEmail("Not correct email");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void testPartialUpdateUser() {
        UpdateUserDto updateUserDto = getUpdateUserDto();

        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(1L), any(UpdateUserDto.class));
    }

    @Test
    @SneakyThrows
    void testPartialUpdateUserThrowsValidationException() {
        UpdateUserDto updateUserDto = getUpdateUserDto();
        updateUserDto.setBirthDate(LocalDate.now());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @SneakyThrows
    void testDeleteUser(){
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }

    @Test
    @SneakyThrows
    void testGetUsersByBirthdateRange(){
        LocalDate from = LocalDate.of(1990,1,1);
        LocalDate to = from.plusYears(10);

        DateRangeDto dateRangeDto = new DateRangeDto(from, to);

        mockMvc.perform(get("/users/getByBirthDateRange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dateRangeDto)))
                .andExpect(status().isOk());

        verify(userService).getUsersByBirthRange(any(DateRangeDto.class));
    }
}
