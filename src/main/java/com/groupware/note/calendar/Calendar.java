package com.groupware.note.calendar;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Calendar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "calendar_id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Users user;
	
	private String title;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime start;
	
	@Column(name = "end_time" , columnDefinition = "DATETIME")
	private LocalDateTime end;
	private String color;
	
	private String departmentName;
	
}
