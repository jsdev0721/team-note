package com.groupware.note.expense;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointInputForm {
	
	@PositiveOrZero(message = "0 이상의 숫자만 입력해주세요")
	private Long depPointPer;
	
	@PositiveOrZero(message = "0 이상의 숫자만 입력해주세요")
	private Long depPointPlus;
	
	@PositiveOrZero(message = "0 이상의 숫자만 입력해주세요")
	private Long individualPoint;
}
