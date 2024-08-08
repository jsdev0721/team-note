package com.groupware.note.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormsForm {
	
	@NotEmpty(message = "제목은 필수 사항입니다")
	private String title;
	
	@NotEmpty(message = "내용은 필수 사항입니다")
	private String content;
	
	private List<MultipartFile> multiPartFile;

}
