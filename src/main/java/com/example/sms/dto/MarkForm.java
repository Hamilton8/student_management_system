package com.example.sms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkForm {

    @NotNull(message = "Select a student")
    private Long studentId;

    @NotNull(message = "Select a subject")
    private Long subjectId;

    @NotNull(message = "Enter the marks")
    @DecimalMin(value = "0.0", message = "Marks cannot be negative")
    private Double marksObtained;
}
