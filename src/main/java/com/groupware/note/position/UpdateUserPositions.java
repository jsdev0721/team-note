package com.groupware.note.position;

import java.time.LocalDateTime;
import com.groupware.note.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UpdateUserPositions {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Users user;
	
	@ManyToOne
	private Positions position;
	
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime localDateTime;
	
	
	
	
	
	
	
	
	

}
