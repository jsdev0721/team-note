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
	
	//작성자
	@ManyToOne
	@JoinColumn(name="writer")
	private UserDetails writer;
	
	
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime useDate;
	//거래처
	
	//내역
	
	//금액
	private double amount;
	
	//비목
	private String expenseType;
	
	//비고
	private String description;
	
	//파일(영수증)
	@ManyToOne
	private Files file;
	
}