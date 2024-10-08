package com.groupware.note.calendar;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
	
	private final UserService userService;
	private final CalendarService calendarService;
	

	@PostMapping("/save")
	@ResponseBody
	public void calendarSave(Principal principal,@RequestParam("title") String title, @RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("color") String color, @RequestParam("check") String check) {
		Users user = this.userService.getUser(principal.getName());
		Calendar calendar = new Calendar();
		if(check.equals("팀")) {
			String name = user.getPosition().getDepartment().getDepartmentName();
			calendar.setDepartmentName(name);
		}
		calendar.setUser(user);
		calendar.setTitle(title);			
		calendar.setStart(LocalDateTime.parse(start));
		calendar.setEnd(LocalDateTime.parse(end));
		calendar.setColor(color);
		this.calendarService.create(calendar);
	}
	
	@GetMapping("/get")
	@ResponseBody
	public List<Calendar> calendarList(Principal principal){
		Users user = this.userService.getUser(principal.getName());
		String departmentName = user.getPosition().getDepartment().getDepartmentName();
		return this.calendarService.calendarList(user, departmentName);
	}
	
	@DeleteMapping("/delete")
	@ResponseBody
	public void calendarDelete(@RequestParam("id") Integer id) {
		this.calendarService.deleteCalendar(this.calendarService.getCalendar(id));
	}
	
}
