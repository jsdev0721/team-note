package com.groupware.note.leave;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.groupware.note.files.Files;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {
	
	private final LeaveRepository leaveRepository;
	
	public void create(Users users, String title, String reason, LocalDate startDate, LocalDate endDate, List<Files> files) {
		Leave leave = new Leave();
		leave.setUser(users);
		leave.setTitle(title);
		leave.setReason(reason);
		leave.setStartDate(startDate);
		leave.setEndDate(endDate);
		if(files != null) {
			leave.setAttachment(files);
		}
		leave.setStatus("queue");
		this.leaveRepository.save(leave);
	}

}
