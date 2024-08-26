package com.groupware.note.calendar;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {
	
	private final CalendarRepository calendarRepository;
	
	
	public void create(Calendar calendar) {
		this.calendarRepository.save(calendar);
	}
	
	public List<Calendar> calendarList(Users user){
		return this.calendarRepository.findByUser(user);
	}
	
	public List<Calendar> calendarList(Users user, String departmentName){
		return this.calendarRepository.findByUserOrDepartmentName(user, departmentName);
	}
	
	public Calendar getCalendar(Integer id) {
		Optional<Calendar> oc = this.calendarRepository.findById(id);
		if(oc.isPresent()) {
			return oc.get();
		}else {
			throw new DataNotFoundException("calendar not found");
		}
	}
	
	public void deleteCalendar (Calendar c) {
		this.calendarRepository.delete(c);
	}
}
