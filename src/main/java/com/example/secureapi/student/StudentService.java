package com.example.secureapi.student;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getStudents() {
    return studentRepository.findAll();

  }

  public void addStudent(Student student) {
    Optional<Student> studentOptional = studentRepository.findStudentByEmail(
        student.getEmail()
    );
    if (studentOptional.isPresent()) {
      throw new IllegalStateException("email already taken");
    }
    studentRepository.save(student);
  }

  public void deleteStudent(Long id) {
    boolean exists = studentRepository.existsById(id);
    if (!exists) {
      throw new IllegalStateException(
          "student with id "
              + id
              + " does not exist"
      );
    }
    studentRepository.deleteById(id);
  }

  @Transactional
  public void updateStudent(Long id, String name, String email) {
    // checking that student exists
    Student student = studentRepository.findById(id).orElseThrow(
        () -> new IllegalStateException(
            "student with id "
                + id
                + " does not exist"
        )
    );

    // name
    if (name != null
        && name.length() > 0
        && !name.equals(student.getName())) {
      student.setName(name);
    }

    // email
    if ((email != null)
        && (email.length() > 0)
        && !email.equals(student.getEmail())) {
      boolean alreadyExists = studentRepository.findStudentByEmail(email)
                                               .isPresent();
      if (alreadyExists) {
        throw new IllegalStateException("email already taken");
      }
      student.setEmail(email);
    }
  }

  public Student getStudent(Long id) {
    return studentRepository.findById(id).orElseThrow(
        () -> new IllegalStateException(
            "student with id "
                + id
                + " does not exist"
        )
    );
  }
}
