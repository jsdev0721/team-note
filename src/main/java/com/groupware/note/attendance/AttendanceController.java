package com.groupware.note.attendance;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	private final UserService userService;
	
	@GetMapping("/checkin")
	public String getCheckIn(Principal principal) {
		Users user = this.userService.getUser(principal.getName());
		System.out.println(user.getStatus());
		if(!user.getStatus().equals("출근")) {
			return "attendanceButton";
		}else {
			return "index";
		}
	}
	
	@PostMapping("/checkin")
	public String postCheckIn(Principal principal, @RequestParam("inlatitude") double lat, @RequestParam("inlongitude") double lon, @RequestParam("reason") String reason) {
		System.out.println(lat);
		System.out.println(lon);
		this.attendanceService.createIn(principal.getName(), lat, lon, reason);
		return "index";
	}
	
	@PostMapping("/checkout")
	public String postCheckOut(Principal principal, @RequestParam("outlatitude") double lat, @RequestParam("outlongitude") double lon) {
		System.out.println(lat);
		System.out.println(lon);
		this.attendanceService.createOut(principal.getName(), lat, lon);
		return "login";
	}
	
	
}
