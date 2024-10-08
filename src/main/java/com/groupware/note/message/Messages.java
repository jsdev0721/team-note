package com.groupware.note.message;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.groupware.note.user.Users;

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
public class Messages {
//message
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private ChatRooms chatRoom;
	
	@ManyToOne
	private Users sender;
	
	private String content;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime sendTime;
	
	@ColumnDefault("0")
	private Integer seenByR;
	
}
