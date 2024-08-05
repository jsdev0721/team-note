package com.groupware.note.user;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserDetails {
	
	@Id
	private Integer userId;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="user_id")
	private Users user;
	
	private String name;
	
	private LocalDate birthdate;
	
	private String email;
	
	private String photo;
	
	private long leave;
	
	private long points;
}
