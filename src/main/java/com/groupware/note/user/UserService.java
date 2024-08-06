package com.groupware.note.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Users create(String username, String password) {
		Optional<Users> option = this.userRepository.findByUsername(username);
		if(option.isPresent()) {
			throw new DataNotFoundException("동일한 아이디가 존재합니다.");
		}
		Users users = new Users();
		users.setUsername(username);
		users.setPassword(passwordEncoder.encode(password));
		return this.userRepository.save(users);
	}
	
	public Users getUser(String username) {
		Optional<Users> users = this.userRepository.findByUsername(username);
		if(users.isPresent()) {
			return users.get();
		}else {
			throw new DataNotFoundException("데이터가 존재하지 않습니다.");
		}
	}
	
	public Users findPW(String username) {
		Optional<Users> users = this.userRepository.findByUsername(username);
		if(users.isPresent()) {
			return users.get();
		}else {
			return new Users();
		}
	}

}
