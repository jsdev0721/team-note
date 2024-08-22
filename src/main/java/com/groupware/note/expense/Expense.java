package com.groupware.note.expense;




import java.time.LocalDateTime;
import java.util.List;

import com.groupware.note.files.Files;
import com.groupware.note.user.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private UserDetails writer;
	
	private String expenseType;
	private double amount;
	private LocalDateTime useDate;
	private String description;
	@ManyToOne
	private Files file;
}