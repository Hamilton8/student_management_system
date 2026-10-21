package com.example.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Roll number is required")
    @Column(unique = true, nullable = false, length = 30)
    private String rollNumber;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @Pattern(regexp = "^$|^[0-9+\\- ]{7,15}$", message = "Enter a valid phone number")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Course is required")
    private String course;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mark> marks = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
