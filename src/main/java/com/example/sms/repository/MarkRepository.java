package com.example.sms.repository;

import com.example.sms.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByStudentId(Long studentId);
    Optional<Mark> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    boolean existsBySubjectId(Long subjectId);
}
