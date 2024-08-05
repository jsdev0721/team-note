package com.groupware.note.approval;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

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
public class Approval {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer approvalId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	private String status;
	
	private String title;
	
	private String content;
	
	private String attachment;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Departments departement;
	
}
