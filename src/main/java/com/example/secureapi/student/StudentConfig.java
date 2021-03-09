package com.example.secureapi.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

  // this runs on every execute
  @Bean
  CommandLineRunner commandLineRunner(
      final StudentRepository studentRepository
  ) {
    return args -> {
      final Student gon = new Student(
          "Gon",
          "gon@hunter.com",
          LocalDate.of(
              1996,
              2,
              9
          )
      );
      final Student killua = new Student(
          "killua",
          "killua@hunter.com",
          LocalDate.of(
              1997,
              2,
              9
          )
      );
      studentRepository.saveAll(
          List.of(
              killua,
              gon
          )
      );
    };
  }
}
