package com.groupware.note.form;

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
public class Forms {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer formId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	private String title;
	
	private String content;
	
	private String attachment;
	
	private LocalDateTime createDate;

}
