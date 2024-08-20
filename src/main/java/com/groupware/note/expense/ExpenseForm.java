package com.groupware.note.expense;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpenseForm {
	
	@NotEmpty
	private List<MultipartFile> multipartFiles;
	
}