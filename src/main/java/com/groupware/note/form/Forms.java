package com.groupware.note.form;

import java.time.LocalDateTime;
import java.util.List;

import com.groupware.note.files.Files;
import com.groupware.note.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime createDate;
	
	@OneToMany
	private List<Files> fileList;


}
