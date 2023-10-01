package com.example.test.service.impl;

import com.example.test.domain.User;
import com.example.test.dto.DateRangeDto;
import com.example.test.dto.UpdateUserDto;
import com.example.test.exception.IncorrectDateException;
import com.example.test.repository.UserRepository;
import com.example.test.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${min.user.age}")
    private Integer minimalAge;
    public static final String USER_EXISTS = "User already exists";
    public static final String USER_NOT_FOUND = "User is not found";
    public static final String INCORRECT_DATE = "Incorrect birth date! User should be at least 18 years old";



    @Override
    public void createUser(User user) {
        if(user.getId() != null && userRepository.existsById(user.getId())){
            throw new EntityExistsException(USER_EXISTS);
        }
        if(!isDateValid(user.getBirthDate())){
            throw new IncorrectDateException(INCORRECT_DATE);
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, User user) {
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        if(!isDateValid(user.getBirthDate())){
            throw new IncorrectDateException(INCORRECT_DATE);
        }

        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        updateUserFieldsFromDto(updateUserDto, user);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByBirthRange(DateRangeDto dateRangeDto) {
        if(dateRangeDto.getFrom().isAfter(dateRangeDto.getTo()))
        {
            throw new IncorrectDateException(INCORRECT_DATE);
        }
        return userRepository.findByBirthDates(dateRangeDto.getFrom(), dateRangeDto.getTo());
    }

    private boolean isDateValid(LocalDate date){

        LocalDate today = LocalDate.now();
        Period period = Period.between(date, today);

        return period.getYears() >= minimalAge;
    }

    private void updateUserFieldsFromDto(UpdateUserDto updateUserDto, User user) {
        if(updateUserDto.getEmail() != null){
            user.setEmail(updateUserDto.getEmail());
        }
        if(updateUserDto.getFirstName() != null){
            user.setFirstName(updateUserDto.getFirstName());
        }
        if(updateUserDto.getLastName() != null){
            user.setLastName(updateUserDto.getLastName());
        }
        if(updateUserDto.getBirthDate() != null){
            isDateValid(updateUserDto.getBirthDate());
            user.setBirthDate(updateUserDto.getBirthDate());
        }
        if(updateUserDto.getAddress() != null){
            user.setAddress(updateUserDto.getAddress());
        }
        if(updateUserDto.getPhoneNumber() != null){
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
    }
}
