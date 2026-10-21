package com.example.sms.service;

import com.example.sms.model.Subject;
import com.example.sms.repository.MarkRepository;
import com.example.sms.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository repo;
    private final MarkRepository markRepo;

    public List<Subject> findAll() {
        return repo.findAll();
    }

    public Subject get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));
    }

    public boolean codeTaken(Subject s) {
        return repo.findByCode(s.getCode())
                .filter(existing -> s.getId() == null || !existing.getId().equals(s.getId()))
                .isPresent();
    }

    public Subject save(Subject s) {
        return repo.save(s);
    }

    public void delete(Long id) {
        if (markRepo.existsBySubjectId(id)) {
            throw new IllegalStateException("Cannot delete: marks have already been entered for this subject.");
        }
        repo.deleteById(id);
    }

    public long count() {
        return repo.count();
    }
}
