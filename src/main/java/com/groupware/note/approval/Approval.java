package com.groupware.note.approval;

import java.time.LocalDateTime;
import java.util.List;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

import jakarta.persistence.CascadeType;
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

	private LocalDateTime updateTime; //0729 추가생성
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Departments department;
	
	@OneToMany(mappedBy = "approval" , cascade = CascadeType.REMOVE)
	private List<ApprovalFile> fileList;
}
