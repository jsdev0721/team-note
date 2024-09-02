package com.groupware.note.position;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Positions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer positionId;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Departments department;
	
	private String positionName;
	
	@OneToMany(mappedBy = "position")
	private List<Users> users;
	
}
