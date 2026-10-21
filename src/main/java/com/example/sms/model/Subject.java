package com.example.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject code is required")
    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotNull(message = "Maximum marks is required")
    @Min(value = 1, message = "Maximum marks must be at least 1")
    private Integer maxMarks = 100;
}
