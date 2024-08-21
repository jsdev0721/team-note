package com.groupware.note.welfaremall;

import java.util.List;

import com.groupware.note.files.Files;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class WelfareMall {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;
	
	private String productName;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Files> photos;
	
	private String description;
	
	private Integer price;
	
}
