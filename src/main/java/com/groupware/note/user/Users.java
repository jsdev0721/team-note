package com.groupware.note.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.groupware.note.position.Positions;


import jakarta.persistence.Column;
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
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	@JsonIgnore
	private Positions position;
	
}
