package com.groupware.note.notice;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeForm {
	
	@NotEmpty(message = "제목은 필수 사항입니다")
	private String title;
	
	@NotEmpty(message = "내용은 필수 사항입니다")
	private String content;
	
	
	private String attachment;

}
