package com.example.test;

import com.example.test.domain.User;

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
}
