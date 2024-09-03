package com.groupware.note.welfaremall;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WelfareMallForm {
	@NotEmpty(message = "제품명을 입력하세요")
	private String productName;
	@NotEmpty(message = "내용을 입력하세요")
	private String desciption;
	@NotNull(message = "가격을 설정하세요.")
	private Integer price;
	
	private List<MultipartFile> fileList;
	
	private MultipartFile mainImage;
	
	private List<String> optionList;
}
