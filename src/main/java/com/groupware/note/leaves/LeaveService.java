package com.groupware.note.leaves;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.calendar.Calendar;
import com.groupware.note.files.Files;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {
	
	private final LeaveRepository leaveRepository;
	
	public void create(Users users, String title, String reason, LocalDate startDate, LocalDate endDate, String status, List<Files> files) {
		Leaves leave = new Leaves();
		leave.setUser(users);
		leave.setTitle(title);
		leave.setReason(reason);
		leave.setStartDate(startDate);
		leave.setEndDate(endDate);
		leave.setStatus(status);
		if(!files.isEmpty()) {
			leave.setAttachment(files);
		}
		this.leaveRepository.save(leave);
	}
	public void deleteLeave(Users user) {
		Optional<Leaves> optional = this.leaveRepository.findByUser(user);
		if(optional.isPresent()) {
			Leaves leave=optional.get();
			leave.setUser(null);
		}
	}
}
