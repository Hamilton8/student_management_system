package com.example.sms.dto;

import com.example.sms.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudentReport {
    private Student student;
    private List<SubjectResult> results;
    private double total;
    private double maxTotal;
    private double percentage;
    private String grade;
    private String result;   // PASS / FAIL
    private Integer rank;    // only set in class reports
}
