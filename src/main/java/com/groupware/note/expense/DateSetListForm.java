package com.groupware.note.expense;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateSetListForm {
	
	private LocalDate startDate;
	
	private LocalDate endDate;
}
