package com.groupware.note.position;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.department.Departments;
import com.groupware.note.user.UserRepository;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService {
	private final PositionRepository positionRepository;
	private final UserRepository userRepository;

	public Positions findById(Integer id) {
		Optional<Positions> _position = this.positionRepository.findById(id);
		if(_position.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _position.get();
	}
	public  void updatePosition(Integer userId,Positions positions) {
		Optional<Users> users = this.userRepository.findById(userId);
		if(users.isPresent()) {
			Users user =users.get();
			user.setPosition(positions);
			this.userRepository.save(user);
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}
	public Positions findByPositionName(String positioName,Departments id) {
		Optional<Positions> positions =this.positionRepository.findByPositionNameAndDepartment(positioName, id);
		if(positions.isPresent()) {
			return positions.get();
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}
}

