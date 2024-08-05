package com.groupware.note.leave;

import java.time.LocalDate;

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
public class Leave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer leaveId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String reason;
	
	private String attachment;
	
	private String status;
	
}
