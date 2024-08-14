package com.groupware.note.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.files.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	
	private final UserDetailsRepository userDetailsRepository;
	
	public void create(Users users, String name, LocalDate brithdate, String email, Files files, Integer leave) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUser(users);
		userDetails.setName(name);
		userDetails.setBirthdate(brithdate);
		userDetails.setEmail(email);
		userDetails.setPhoto(files);
		userDetails.setLeave(leave);
		this.userDetailsRepository.save(userDetails);
	}
	
	@Scheduled(cron = "0 0 1 1 1 ?")
	public void updateLeave() { //매년 1월 1일에 업데이트
		List<UserDetails> userDetails = this.userDetailsRepository.findAll();
		
	}
	
	public void minusLeave(UserDetails userDetails, Integer leaveDate) {
		UserDetails user = userDetails;
		Integer leave = user.getLeave() - (leaveDate+1);
		user.setLeave(leave);
		this.userDetailsRepository.save(user);
	}
	
	public UserDetails findByUser(Users users) {
		Optional<UserDetails> optional = this.userDetailsRepository.findById(users.getUserId());
		if(optional.isPresent()) {
			return optional.get();
		}else {
			return new UserDetails();
		}
	}
	
	public UserDetails findByName(String name) {
		Optional<UserDetails> optional = this.userDetailsRepository.findByName(name);
		if(optional.isPresent()) {
			return optional.get();
		}else {
			return new UserDetails();
		}
	}
	
	public UserDetails findID(String email) {
		Optional<UserDetails> userDetails = this.userDetailsRepository.findByEmail(email);
		if(userDetails.isPresent()) {
			return userDetails.get();
		}else {
			return new UserDetails();
		}
	}
	
	public void uploadPhoto(Users users, Files photo) {
		Optional<UserDetails> userDetails = this.userDetailsRepository.findById(users.getUserId());
		if(userDetails.isPresent()) {
			UserDetails user = userDetails.get();
			user.setPhoto(photo);
			this.userDetailsRepository.save(user);
		}
	}
	public List<UserDetails> userfindByAll() {
		return this.userDetailsRepository.findAll();
	}
	public UserDetails getUser(Integer userId) {
		Optional<UserDetails> getUser = this.userDetailsRepository.findById(userId);
		if(getUser.isPresent()) { 
			return getUser.get();
		}else { throw new DataNotFoundException("데이터를 찾을 수 없습니다");}
		
	
		
		
		
		
	}
	

}
