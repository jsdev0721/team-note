package com.groupware.note.position;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groupware.note.user.UserRepository;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UpdateUserPositionsService {
	
	private final UpdatePositionsRepository updatePositionsRepository;
	private final UserRepository userRepository;
	
	@Scheduled(cron = " 0 0 10 * * * " )
	//@Scheduled(cron = " 0 0/3 * * * ? " )
	//@Scheduled(cron = " 0 * 14 * * * " )
	public void updatePositions() {
		List<UpdateUserPositions> list = this.updatePositionsRepository.findAll();
		for(UpdateUserPositions updateUserPositions : list) {
			  Duration duration = Duration.between(LocalDateTime.now(),updateUserPositions.getLocalDateTime());
			  System.out.println(duration);
			 if(duration.isNegative()) {
				 Users users = updateUserPositions.getUser();
				 users.setPosition(updateUserPositions.getPosition());
				 this.userRepository.save(users);
				 this.updatePositionsRepository.delete(updateUserPositions);
			 }
		}
	}
	public List<UpdateUserPositions> userfindByAll() {
		return this.updatePositionsRepository.findAll();
		
	}

}
