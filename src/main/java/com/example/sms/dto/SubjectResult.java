package com.example.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubjectResult {
    private String subjectCode;
    private String subjectName;
    private int maxMarks;
    private double marksObtained;
    private boolean passed;
}
