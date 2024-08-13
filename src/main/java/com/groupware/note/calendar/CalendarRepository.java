package com.groupware.note.calendar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.note.user.Users;

public interface CalendarRepository extends JpaRepository<Calendar, Integer>{
	
	List<Calendar> findByUser(Users user);

}
