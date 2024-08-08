package com.groupware.note.user;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.files.Files;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	
	private final UserDetailsRepository userDetailsRepository;
	
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

}
