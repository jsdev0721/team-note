package com.groupware.note.attendance;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class Attendance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attendanceId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime checkInTime;
	private double inTimeLat;
	private double inTimeLon;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime checkOutTime;
	private double outTimeLat;
	private double outTimeLon;
	@Column(columnDefinition = "TIME")
	private LocalTime workTime;
	@Column(columnDefinition = "TIME")
	private LocalTime overTime;
	
	private String reason;
	
}
