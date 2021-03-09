package com.example.secureapi.student;

import java.time.LocalDate;
import java.util.List;

import com.example.secureapi.models.Student;
import com.example.secureapi.security.ApplicationUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StudentsConfig {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public StudentsConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository repository) {
		return (args -> {
			final Student user =
					new Student(
						"user",
						passwordEncoder.encode("0000"),
						"user@spring.co",
						ApplicationUserRole.STUDENT,
						LocalDate.of(1997, 2, 9)
					);
			final Student trainee =
					new Student(
						"trainee",
						passwordEncoder.encode("0000"),
						"trainee@spring.co",
						ApplicationUserRole.ADMIN_TRAINEE,
						LocalDate.of(1996, 2, 9)
					);
			final Student admin =
					new Student(
						"admin",
						passwordEncoder.encode("0000"),
						"admin@spring.co",
						ApplicationUserRole.ADMIN,
						LocalDate.of(1995, 2, 9)
					);
			repository.saveAll(List.of(user, trainee, admin));

		});

	}

}
