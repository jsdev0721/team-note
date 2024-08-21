package com.groupware.note.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.note.user.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ChatRooms {
//Conversation
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Users user1;
	
	@ManyToOne
	private Users user2;
	
	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Messages> messages;
}
