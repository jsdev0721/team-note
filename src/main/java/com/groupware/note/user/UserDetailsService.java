package com.groupware.note.user;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

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

}
