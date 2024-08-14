package com.groupware.note.department;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {
	private final DepartmentRepository departmentRepository;
	
	public Departments findBydepartmentName(String departmentName) {
		Optional<Departments> _departments = this.departmentRepository.findByDepartmentName(departmentName);
		if(_departments.isEmpty()) {
			throw new DataNotFoundException("부서가 존재하지 않습니다.");
		}
		return _departments.get(); 
	}
	
}
