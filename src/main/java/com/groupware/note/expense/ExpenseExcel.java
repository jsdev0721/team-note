package com.groupware.note.expense;




import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ExpenseExcel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long expenseDataId;
	private String writer;	
	private String expenseType;
	private double amount;
	private LocalDateTime useDate;
	private String description;
	
	@OneToMany(mappedBy = "expenseExcel", cascade = CascadeType.REMOVE)
	private List<ExpensePhoto> expensePhotoList;
}
