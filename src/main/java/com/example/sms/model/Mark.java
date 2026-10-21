package com.example.sms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(nullable = false)
    private Double marksObtained;
}
