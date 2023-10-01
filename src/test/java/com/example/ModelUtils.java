package com.example;

import com.example.domain.User;
import com.example.dto.UpdateUserDto;

import java.time.LocalDate;

public class ModelUtils {

    public static User getUser(){
        User user = new User();
        user.setEmail("user@mail.com");
        user.setFirstName("User");
        user.setLastName("User");
        user.setBirthDate(LocalDate.EPOCH);
        return user;
    }

    public static UpdateUserDto getUpdateUserDto(){
        return UpdateUserDto.builder()
                .email("user@mail.com")
                .firstName("User")
                .lastName("New name")
                .phoneNumber("+380000000000")
                .address("New address")
                .birthDate(LocalDate.EPOCH)
                .build();
    }
}
