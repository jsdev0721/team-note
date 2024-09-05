package com.groupware.note.expense;




import java.time.LocalDateTime;

import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long expenseDataId;
	
	@ManyToOne
	@JoinColumn(name="writer")
	private UserDetails writer;
	
	private String expenseType;
	private double amount;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime useDate;
	private String description;
	@ManyToOne
	private Files file;
	
}