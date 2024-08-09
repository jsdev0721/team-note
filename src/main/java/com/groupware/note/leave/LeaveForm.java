package com.groupware.note.leave;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.groupware.note.files.Files;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveForm {
	
	@NotEmpty(message = "휴가 시작 날짜를 입력해 주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotEmpty(message = "휴가 종료 날짜를 입력해 주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@NotEmpty(message = "휴가 사유를 입력해 주세요.")
	private String reason;
	
	private List<Files> attachment;

}
