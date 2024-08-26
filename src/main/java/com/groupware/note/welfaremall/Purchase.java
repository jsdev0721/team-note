package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Purchase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purchaseId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Departments department;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "cartList_id")
	private List<WelfareMall> productList;
	
	private Integer quantity;
	
	private Integer totalPrice;
	
	private String purchaseStatus;
	
	private LocalDateTime purchaseDate;
	
	private String purchaseType;
	
}
