package com.example.contoller;

import com.example.domain.User;
import com.example.dto.DateRangeDto;
import com.example.dto.UpdateUserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            throw new ValidationException(bindingResult.getAllErrors().toString());
//        }
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getByBirthDateRange")
    public ResponseEntity<List<User>> getUsers(@RequestBody @Valid DateRangeDto dateRangeDto, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            throw new ValidationException(bindingResult.getAllErrors().toString());
//        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersByBirthRange(dateRangeDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UpdateUserDto updateUserDto, @PathVariable Long id, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            throw new ValidationException(bindingResult.getAllErrors().toString());
//        }
        userService.updateUser(id, updateUserDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid User user, @PathVariable Long id, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            throw new ValidationException(bindingResult.getAllErrors().toString());
//        }
        userService.updateUser(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
