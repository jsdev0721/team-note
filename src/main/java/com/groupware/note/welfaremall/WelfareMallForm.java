package com.groupware.note.welfaremall;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WelfareMallForm {
	@NotEmpty
	private String productName;
	@NotEmpty
	private String desciption;
	
	private Integer price;
	
	private List<MultipartFile> fileList;
}
