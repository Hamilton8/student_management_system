package com.example.sms.service;

import com.example.sms.dto.StudentReport;
import com.example.sms.dto.SubjectResult;
import com.example.sms.model.Mark;
import com.example.sms.model.Student;
import com.example.sms.repository.MarkRepository;
import com.example.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    /** A subject is passed when the student scores at least this % of its max marks. */
    public static final double PASS_PERCENT = 40.0;

    private final StudentRepository studentRepo;
    private final MarkRepository markRepo;

    @Transactional(readOnly = true)
    public StudentReport studentReport(Long studentId) {
        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        return build(s, markRepo.findByStudentId(studentId));
    }

    /** Ranked report of every student who has at least one mark (optionally filtered by course). */
    @Transactional(readOnly = true)
    public List<StudentReport> classReport(String course) {
        List<StudentReport> reports = new ArrayList<>();
        for (Student s : studentRepo.findAll()) {
            if (course != null && !course.isBlank() && !course.equalsIgnoreCase(s.getCourse())) continue;
            List<Mark> marks = markRepo.findByStudentId(s.getId());
            if (!marks.isEmpty()) reports.add(build(s, marks));
        }
        reports.sort(Comparator.comparingDouble(StudentReport::getPercentage).reversed());
        int rank = 0, position = 0;
        double previous = -1;
        for (StudentReport r : reports) {          // ties share a rank
            position++;
            if (r.getPercentage() != previous) { rank = position; previous = r.getPercentage(); }
            r.setRank(rank);
        }
        return reports;
    }

    @Transactional(readOnly = true)
    public List<String> courses() {
        return studentRepo.findAll().stream().map(Student::getCourse).distinct().sorted().toList();
    }

    private StudentReport build(Student s, List<Mark> marks) {
        List<SubjectResult> results = marks.stream()
                .map(m -> new SubjectResult(
                        m.getSubject().getCode(),
                        m.getSubject().getName(),
                        m.getSubject().getMaxMarks(),
                        m.getMarksObtained(),
                        m.getMarksObtained() >= m.getSubject().getMaxMarks() * PASS_PERCENT / 100.0))
                .sorted(Comparator.comparing(SubjectResult::getSubjectCode))
                .toList();

        double total = results.stream().mapToDouble(SubjectResult::getMarksObtained).sum();
        double max = results.stream().mapToDouble(SubjectResult::getMaxMarks).sum();
        double pct = max == 0 ? 0 : Math.round(total * 10000.0 / max) / 100.0;
        boolean allPassed = !results.isEmpty() && results.stream().allMatch(SubjectResult::isPassed);

        return new StudentReport(s, results, total, max, pct, gradeFor(pct), allPassed ? "PASS" : "FAIL", null);
    }

    static String gradeFor(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        if (pct >= PASS_PERCENT) return "E";
        return "F";
    }
}
