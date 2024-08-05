package com.groupware.note.message;

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
public class MessageContents {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer messageId;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private MessageRooms room;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private Users sender;
	
	private String content;
	
	private String attachment;
	
	private LocalDateTime sendDate;
	
}
