package com.example.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Enter valid email")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name should not be blank")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name should not be blank")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "birth_date", nullable = false)
    @Past(message = "Birth date must must be earlier than the current date")
    private LocalDate birthDate;

    @Column
    private String address;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+\\d{1,3}\\s?\\d{1,}$", message = "Enter valid phone number")
    private String phoneNumber;
}
