package com.groupware.note.position;



import java.time.LocalDateTime;
import java.util.List;
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
	private final UpdatePositionsRepository updatePositionsRepository;

	public Positions findById(Integer id) {
		Optional<Positions> _position = this.positionRepository.findById(id);
		if(_position.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _position.get();
	}
	public  void updatePosition(Integer userId,Positions positions,LocalDateTime localDateTime) {
		Optional<Users> users = this.userRepository.findById(userId);
		if(users.isPresent()) {
			Users user =users.get();
			UpdateUserPositions updateUserPositions = new UpdateUserPositions();
			updateUserPositions.setUser(user);
			updateUserPositions.setPosition(positions);
			updateUserPositions.setLocalDatetime(localDateTime);
			this.updatePositionsRepository.save(updateUserPositions);
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}
	public Positions findByPositionNameAndDepartment(String positioName,Departments id) {
		Optional<Positions> positions =this.positionRepository.findByPositionNameAndDepartment(positioName, id);
		if(positions.isPresent()) {
			return positions.get();
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}
	
	public void insertPositionIntoNewDep(Departments dep) {
		String[] positionset = {"section chief", "deputy", "worker" , "intern"};
		
		for(String position : positionset) {
			Positions pos = new Positions();
			pos.setDepartment(dep);
			pos.setPositionName(position);
			positionRepository.save(pos);
			
		}
	}
	
	public void deletePositionOfDep(Departments dep) {
		List<Positions> _pList = this.positionRepository.findAllByDepartment(dep);
		for(Positions p : _pList) {
			this.positionRepository.delete(p);
		}
	}
}

