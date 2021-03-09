package com.example.secureapi.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

	private final StudentService studentService;

	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public List<Student> getStudents() {
		return studentService.getStudents();
	}

	@GetMapping(path = "{studentId}")
	public Student getStudent(@PathVariable("studentId") Long id) {
		return studentService.getStudent(id);
	}

	@PostMapping
	public void registerNewStudent(@RequestBody Student student) {
		studentService.addStudent(student);
	}

	@DeleteMapping(path = "{studentId}")
	public void deleteStudent(@PathVariable("studentId") Long id) {
		studentService.deleteStudent(id);
	}

	@PutMapping(path = "{studentId}")
	public void updateStudent(
			@PathVariable("studentId") Long id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email
	) {
		studentService.updateStudent(
				id,
				name,
				email
		);
	}
}
