package com.groupware.note.calendar;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
