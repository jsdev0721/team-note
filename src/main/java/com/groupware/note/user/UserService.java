package com.groupware.note.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.position.PositionService;
import com.groupware.note.position.Positions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PositionService positionService;
	
	public Users create(String username, String password) {
		Optional<Users> option = this.userRepository.findByUsername(username);
		if(option.isPresent()) {
			throw new DataNotFoundException("동일한 아이디가 존재합니다.");
		}
		Users users = new Users();
		users.setUsername(username);
		users.setPassword(passwordEncoder.encode(password));
		users.setStatus("대기");
		users.setPosition(this.positionService.findById(Integer.parseInt("14")));
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
	

	public Users getUser(Integer userId) {
		Optional<Users> users = this.userRepository.findById(userId);
		if(users.isPresent()) {
			return users.get();
		}else {
			throw new DataNotFoundException("데이터가 존재하지 않습니다.");
		}
	}
	public boolean checkPW(String username) {
		Optional<Users> users = this.userRepository.findByUsername(username);
		if(!ObjectUtils.isEmpty(users)) {
			return true;
		}else {
			return false;
		}
	}
	public void changePW(String username, String password) {
		Optional<Users> _users = this.userRepository.findByUsername(username);
		if(_users.isPresent()) {
			Users users = _users.get();
			users.setPassword(passwordEncoder.encode(password));
			this.userRepository.save(users);
		}else {
			throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
		}

	}

}
