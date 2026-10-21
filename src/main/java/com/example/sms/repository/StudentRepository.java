package com.example.sms.repository;

import com.example.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);

    @Query("""
           select s from Student s
           where lower(s.rollNumber) like lower(concat('%', :q, '%'))
              or lower(s.firstName)  like lower(concat('%', :q, '%'))
              or lower(s.lastName)   like lower(concat('%', :q, '%'))
              or lower(s.course)     like lower(concat('%', :q, '%'))
           order by s.rollNumber
           """)
    List<Student> search(@Param("q") String q);
}
