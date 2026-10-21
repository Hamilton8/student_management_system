package com.example.sms.service;

import com.example.sms.dto.MarkForm;
import com.example.sms.model.Mark;
import com.example.sms.model.Student;
import com.example.sms.model.Subject;
import com.example.sms.repository.MarkRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepo;
    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;

    @Transactional(readOnly = true)
    public List<Mark> forStudent(Long studentId) {
        return markRepo.findByStudentId(studentId).stream()
                .sorted(Comparator.comparing(m -> m.getSubject().getCode()))
                .toList();
    }

    public Optional<Mark> find(Long studentId, Long subjectId) {
        return markRepo.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    /** Creates the mark, or updates it if this student already has marks for the subject. */
    @Transactional
    public Mark save(MarkForm form) {
        Student student = studentRepo.findById(form.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Subject subject = subjectRepo.findById(form.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        if (form.getMarksObtained() > subject.getMaxMarks()) {
            throw new IllegalArgumentException(
                    "Marks cannot exceed " + subject.getMaxMarks() + " for " + subject.getName());
        }
        Mark mark = markRepo.findByStudentIdAndSubjectId(student.getId(), subject.getId()).orElseGet(Mark::new);
        mark.setStudent(student);
        mark.setSubject(subject);
        mark.setMarksObtained(form.getMarksObtained());
        return markRepo.save(mark);
    }

    public void delete(Long markId) {
        markRepo.deleteById(markId);
    }

    public long count() {
        return markRepo.count();
    }
}
