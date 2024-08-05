package com.groupware.note.message;

import java.time.LocalDateTime;
import java.util.List;

import com.groupware.note.user.Users;

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
public class MessageRooms {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roomId;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private Users sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private Users receiver;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "room")
	private List<MessageContents> contents;
}
