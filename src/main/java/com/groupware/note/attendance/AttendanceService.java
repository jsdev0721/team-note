package com.groupware.note.attendance;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.groupware.note.user.UserRepository;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	
	public void createIn(String username, double lat, double lon, String reason) {
		Users user = new Users();
		user = this.userService.getUser(username);
		
		Attendance a = new Attendance();
		a.setUser(user);
		a.setInTimeLat(lat);
		a.setInTimeLon(lon);
		a.setCheckInTime(LocalDateTime.now());
		a.setReason(reason);
		this.attendanceRepository.save(a);
		
		user.setStatus("출근");
		this.userRepository.save(user);
	}
	
	public void createOut(String username, double lat, double lon) {
		Users user = new Users();
		user = this.userService.getUser(username);		Attendance attendance = this.attendanceRepository.findLatestCheckInByUser(username);
		attendance.setCheckOutTime(LocalDateTime.now());
		attendance.setOutTimeLat(lat);
		attendance.setOutTimeLon(lon);
		this.attendanceRepository.save(attendance);
		
		user.setStatus("퇴근"); // 유저 상태 출근으로 변경
		this.userRepository.save(user);
	}
	
}
