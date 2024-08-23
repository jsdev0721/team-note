package com.groupware.note.attendance;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String getList(Principal principal, Model model) {
		Users user = this.userService.getUser(principal.getName()); // 유저 정보 불러오기
		List<String> dateList = this.attendanceService.getDateList(user.getUserId()); // "월-별" String List 불러오기
		String date = dateList.get(dateList.size() - 1); // 맨 마지막 날짜를 String date 에 저장
		dateList.remove(dateList.size() - 1); // 해당 내용 삭제 (html에서 select에 표시안하게 하려고)
		dateList.sort(Comparator.reverseOrder()); // 역순정렬
		model.addAttribute("date", date); // select 기본글
		model.addAttribute("dateList", dateList); // select 내용물
		
		List<Attendance> attendanceList = this.attendanceService.getAttendanceList(user.getUserId(), date); // 맨마지막날짜(년-월)에 해당하는 데이터 가져오기 
		model.addAttribute("attendanceList", attendanceList);
		return "attendanceList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/list")
	public String postList(Principal principal, Model model, @RequestParam("date") String date) {
		Users user = this.userService.getUser(principal.getName());
		List<String> dateList = this.attendanceService.getDateList(user.getUserId());
		dateList.remove(new String(date));
		dateList.sort(Comparator.reverseOrder());
		model.addAttribute("date", date);
		model.addAttribute("dateList", dateList);
		
		List<Attendance> attendanceList = this.attendanceService.getAttendanceList(user.getUserId(), date);
		model.addAttribute("attendanceList", attendanceList);
		return "attendanceList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/checkin")
	public String getCheckIn(Principal principal, Model model) {
		Users user = this.userService.getUser(principal.getName());
		if(!user.getStatus().equals("출근")) {
			return "attendanceButton";
		}else {
			return "redirect:/";
		}
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkin")
	public String postCheckIn(Principal principal, @RequestParam("inlatitude") double lat, @RequestParam("inlongitude") double lon, @RequestParam("reason") String reason) {
		System.out.println(lat);
		System.out.println(lon);
		this.attendanceService.createIn(principal.getName(), lat, lon, reason);
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public String postCheckOut(Principal principal, @RequestParam("outlatitude") double lat, @RequestParam("outlongitude") double lon, Model model) {
		System.out.println(lat);
		System.out.println(lon);
		boolean check = this.attendanceService.createOut(principal.getName(), lat, lon);
		model.addAttribute("check", check);
		return "redirect:/user/logout";
	}
	
	
}
