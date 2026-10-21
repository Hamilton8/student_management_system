package com.example.sms.config;

import com.example.sms.dto.MarkForm;
import com.example.sms.model.Student;
import com.example.sms.model.Subject;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.SubjectRepository;
import com.example.sms.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/** Seeds a little demo data on first run (only when the tables are empty). */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final StudentRepository students;
    private final SubjectRepository subjects;
    private final MarkService marks;

    @Override
    public void run(String... args) {
        if (subjects.count() > 0 || students.count() > 0) return;

        List<Subject> subs = subjects.saveAll(List.of(
                subject("MATH101", "Mathematics"), subject("PHY101", "Physics"),
                subject("CHE101", "Chemistry"), subject("ENG101", "English"),
                subject("CS101", "Computer Science")));

        List<Student> list = students.saveAll(List.of(
                student("CS2024001", "Asha", "Reddy", "asha@example.com", "Computer Science", LocalDate.of(2005, 3, 14)),
                student("CS2024002", "Ravi", "Kumar", "ravi@example.com", "Computer Science", LocalDate.of(2005, 7, 2)),
                student("EC2024001", "Meera", "Sharma", "meera@example.com", "Electronics", LocalDate.of(2004, 11, 21))));

        double[][] scores = {{92, 85, 78, 88, 95}, {70, 62, 35, 74, 81}, {55, 48, 66, 59, 72}};
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < subs.size(); j++) {
                MarkForm f = new MarkForm();
                f.setStudentId(list.get(i).getId());
                f.setSubjectId(subs.get(j).getId());
                f.setMarksObtained(scores[i][j]);
                marks.save(f);
            }
        }
    }

    private Subject subject(String code, String name) {
        Subject s = new Subject();
        s.setCode(code); s.setName(name); s.setMaxMarks(100);
        return s;
    }

    private Student student(String roll, String fn, String ln, String email, String course, LocalDate dob) {
        Student s = new Student();
        s.setRollNumber(roll); s.setFirstName(fn); s.setLastName(ln);
        s.setEmail(email); s.setCourse(course); s.setDateOfBirth(dob);
        return s;
    }
}
