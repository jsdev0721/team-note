package com.groupware.note.welfaremall;

import java.time.LocalDateTime;

import com.groupware.note.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private WelfareMall product;
	//양
	private Integer quantity;
	//가격
	private Integer point;
	
	private LocalDateTime addDate;
	//개인/부서
	private String type;
	//옵션(템 종류)
	private String option;
	//queue/process/complete
	private String status;
}
