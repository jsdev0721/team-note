package com.groupware.note.attendance;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("/checkin")
	public String checkIn() {
		return "attendanceButton";
	}
	
	@PostMapping("/checkin")
	public String checkIn(Principal principal) {
		return "index";
	}
	
	
	
}
