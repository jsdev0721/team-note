package com.groupware.note.leaves;

import java.time.LocalDate;
import java.util.List;

import com.groupware.note.files.Files;
import com.groupware.note.user.Users;

import jakarta.persistence.Column;
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
public class Leaves {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer leaveId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	private String title;
	@Column(columnDefinition = "DATE")
	private LocalDate startDate;
	@Column(columnDefinition = "DATE")
	private LocalDate endDate;
	
	private String reason;
	
	@OneToMany
	private List<Files> attachment;
	
	private String status;
	
}
