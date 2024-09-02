package com.groupware.note.leaves;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveForm {
	
	@NotEmpty
	private String departmentName;
	
	@NotEmpty(message = "제목을 입력해 주세요.")
	private String title;
	
	@NotNull(message = "휴가 시작 날짜를 입력해 주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotNull(message = "휴가 종료 날짜를 입력해 주세요.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@NotEmpty(message = "휴가 사유를 입력해 주세요.")
	private String reason;
	
	private List<MultipartFile> attachment;

}
