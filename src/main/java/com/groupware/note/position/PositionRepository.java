package com.groupware.note.position;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.note.department.Departments;


public interface PositionRepository extends JpaRepository<Positions, Integer>{
	
	Optional<Positions> findByPositionName(String positionName);
	Optional<Positions> findByPositionNameAndDepartment(String positionName,Departments id);
	//Optional<Positions> findByUsers(Users user);
	
}
