package com.example.sms.service;

import com.example.sms.model.Student;
import com.example.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repo;

    public List<Student> findAll(String keyword) {
        return (keyword == null || keyword.isBlank()) ? repo.findAll() : repo.search(keyword.trim());
    }

    public List<Student> findAll() {
        return repo.findAll();
    }

    public Student get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    public boolean rollNumberTaken(Student s) {
        return repo.findByRollNumber(s.getRollNumber())
                .filter(existing -> s.getId() == null || !existing.getId().equals(s.getId()))
                .isPresent();
    }

    @Transactional
    public Student save(Student s) {
        if (s.getId() != null) {                       // keep existing marks when editing
            Student existing = get(s.getId());
            s.setMarks(existing.getMarks());
        }
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long count() {
        return repo.count();
    }
}
