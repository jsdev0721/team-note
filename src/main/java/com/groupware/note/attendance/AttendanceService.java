package com.groupware.note.attendance;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	
}
