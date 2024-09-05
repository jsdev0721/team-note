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
	
	public void create(Users users, String name, LocalDate brithdate, String email, Integer leave) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUser(users);
		userDetails.setName(name);
		userDetails.setBirthdate(brithdate);
		userDetails.setEmail(email);
		userDetails.setLeaves(leave);
		this.userDetailsRepository.save(userDetails);
	}
	
	@Scheduled(cron = "0 0 1 1 1 ?")
	public void updateLeave() { //매년 1월 1일에 업데이트
		List<UserDetails> userDetails = this.userDetailsRepository.findAll();
		for(UserDetails user : userDetails) {
			user.setLeaves(15);
			this.userDetailsRepository.save(user);
		}
	}
	
	public void minusLeave(UserDetails user, Integer leaveDate) {
		Integer leave = user.getLeaves() - (leaveDate+1);
		user.setLeaves(leave);
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
			throw new DataNotFoundException("데이터를 찾을 수 없습니다");
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
		}else {throw new DataNotFoundException("데이터를 찾을 수 없습니다");}
	}
	
	public List<UserDetails> searchList(String name){
		return this.userDetailsRepository.findByNameLike("%"+name+"%");
	}

	public void deleteUserDetails(Users users) {
		Optional<UserDetails> optional = this.userDetailsRepository.findByUser(users);
		if(optional.isPresent()) {
			 this.userDetailsRepository.delete(optional.get());
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}

	
	public void deletePhoto(UserDetails userDetails) {
		UserDetails user = userDetails;
		user.setPhoto(null);
		this.userDetailsRepository.save(user);

	}
	public UserDetails save(UserDetails userDetails) {
		return this.userDetailsRepository.save(userDetails);
	}
}
