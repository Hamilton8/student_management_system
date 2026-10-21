# Student Management System

Spring Boot 3 · Spring Data JPA · MySQL · Thymeleaf (Bootstrap 5 via CDN)

## Features
- **Students**: add, view, edit, delete, search (roll no / name / course) with validation and duplicate roll-number check
- **Subjects**: add, edit, delete (deleting is blocked if marks exist)
- **Marks entry**: pick a student → add or update marks per subject (validated against the subject's max marks)
- **Reports**
  - Printable individual report card (total, percentage, grade, pass/fail per subject)
  - Ranked class report with course filter, print view and CSV export

## Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8 running locally
- Lombok support in your IDE (IntelliJ: built in; Eclipse/VS Code: install the Lombok plugin)

## Run
1. Edit `src/main/resources/application.properties` and set your MySQL username/password.
   The `student_db` database is created automatically.
2. `mvn spring-boot:run`
3. Open http://localhost:8080

Demo subjects, students and marks are seeded on first start if the tables are empty
(remove `config/DataLoader.java` if you don't want that).

## Grading rules (`ReportService`)
| % | Grade | | % | Grade |
|---|---|---|---|---|
| ≥ 90 | A+ | | ≥ 60 | C |
| ≥ 80 | A  | | ≥ 50 | D |
| ≥ 70 | B  | | ≥ 40 | E, else F |

A student passes only if every subject is ≥ 40% of its max marks (`PASS_PERCENT`).

## Structure
```
com.example.sms
├── model        Student, Subject, Mark (JPA entities)
├── repository   Spring Data JPA repositories
├── service      Business logic (+ ReportService)
├── dto          MarkForm, StudentReport, SubjectResult
├── controller   Home, Student, Subject, Mark, Report
└── config       DataLoader (sample data)
templates/       Thymeleaf views (fragments, students, subjects, marks, reports)
```
