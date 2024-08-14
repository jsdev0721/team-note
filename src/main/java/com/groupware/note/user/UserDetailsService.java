package com.groupware.note.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.Departments;
import com.groupware.note.files.Files;
import com.groupware.note.position.PositionRepository;
import com.groupware.note.position.Positions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	
	private final UserDetailsRepository userDetailsRepository;
	private final UserRepository userRepository;
	
	public void create(Users users, String name, LocalDate brithdate, String email, Files files) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUser(users);
		userDetails.setName(name);
		userDetails.setBirthdate(brithdate);
		userDetails.setEmail(email);
		userDetails.setPhoto(files);
		this.userDetailsRepository.save(userDetails);
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
	}public List<UserDetails> userfindByAll() {
		return this.userDetailsRepository.findAll();
	}
	public UserDetails getUser(Integer userId) {
		Optional<UserDetails> getUser = this.userDetailsRepository.findById(userId);
		if(getUser.isPresent()) { 
			return getUser.get();
		}else { throw new DataNotFoundException("데이터를 찾을 수 없습니다");}
		
	
		
		
		
		
	}
	

}
