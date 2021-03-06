package com.example.secureapi.student;

import java.util.Optional;

import com.example.secureapi.models.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
  Optional<Student> findStudentByEmail(String email);
}
