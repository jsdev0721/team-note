package com.groupware.note.user;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	
	private final UserDetailsRepository userDetailsRepository;
	
	public void create(Users users, String name, LocalDate brithdate, String email) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUser(users);
		userDetails.setName(name);
		userDetails.setBirthdate(brithdate);
		userDetails.setEmail(email);
		this.userDetailsRepository.save(userDetails);
	}
	
	public UserDetails findID(String email) {
		Optional<UserDetails> userDetails = this.userDetailsRepository.findByEmail(email);
		if(userDetails.isPresent()) {
			return userDetails.get();
		}else {
			throw new DataNotFoundException("일치하는 이메일이 존재하지 않습니다.");
		}
	}

}
