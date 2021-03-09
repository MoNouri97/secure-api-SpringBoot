package com.example.secureapi.models;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.secureapi.security.ApplicationUserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class Student {
	@Id
	@SequenceGenerator(sequenceName = "student_seq", name = "student_seq", allocationSize = 1)
	@GeneratedValue(generator = "student_seq", strategy = GenerationType.SEQUENCE)
	private Long id;

	private String name;
	@JsonIgnore
	private String password;
	private String email;
	private ApplicationUserRole role;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dob;

	@Transient
	private int age;

	public Student() {
	}

	public Student(
			Long id,
			String name,
			String password,
			String email,
			ApplicationUserRole role,
			LocalDate dob
	)
	{
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.dob = dob;
	}

	public Student(
			String name,
			String password,
			String email,
			ApplicationUserRole role,
			LocalDate dob

	)
	{
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.dob = dob;
	}

	// #region getters , setters , toString
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public int getAge() {
		return Period.between(dob, LocalDate.now()).getYears();
	}

	public void setAge(int age) {
		this.age = age;
	}

	public ApplicationUserRole getRole() {
		return role;
	}

	public void setRole(ApplicationUserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Student [age="
				+ age
				+ ", dob="
				+ dob
				+ ", email="
				+ email
				+ ", id="
				+ id
				+ ", name="
				+ name
				+ "]";
	}
	// #endregion

}
