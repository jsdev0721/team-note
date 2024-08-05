package com.groupware.note.approval;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalForm {
	@NotEmpty(message = "제목을 입력하세요.")
	private String title;
	@NotEmpty(message = "내용을 입력히세요.")
	private String content;
	@NotEmpty
	private String departmentName;
	
	private List<MultipartFile> multipartFiles;
}
