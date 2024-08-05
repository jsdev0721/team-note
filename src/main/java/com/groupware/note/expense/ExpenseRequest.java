package com.groupware.note.expense;

import java.time.LocalDateTime;

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
public class ExpenseRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer expenseRequestId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	private String expenseType;
	private double amount;
	private String description;
	private String attachment;
	private String status;
	private LocalDateTime updateDate;
	
}
