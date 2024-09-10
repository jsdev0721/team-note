package com.groupware.note.department;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.position.PositionRepository;
import com.groupware.note.position.PositionService;
import com.groupware.note.position.Positions;
import com.groupware.note.user.UserRepository;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {
	
	private final DepartmentRepository departmentRepository;
	private final PositionService pService;
	private final PositionRepository pRepo;
	private final UserRepository uRepo;

	public Departments findBydepartmentName(String departmentName) {
		Optional<Departments> _departments = this.departmentRepository.findByDepartmentName(departmentName);
		if(_departments.isEmpty()) {
			throw new DataNotFoundException("부서가 존재하지 않습니다.");
		}
		return _departments.get(); 
	}
	
	
	public List<Departments> findAll(){
		List<Departments> _dep = this.departmentRepository.findAll();
		return _dep;
	}
	
	public Departments save(Departments departments) {
		return this.departmentRepository.save(departments);
	}
	
	//부서 생성
	public String createNewDep(String depName ) {
		Optional<Departments> depExist = this.departmentRepository.findByDepartmentName(depName);
		if(depExist.isEmpty() && depName!=null && !depName.equals("noString")) {
			Departments newDep = new Departments();
			newDep.setDepartmentName(depName);
			newDep.setPoints((long) 0);
			this.departmentRepository.save(newDep);
			this.pService.insertPositionIntoNewDep(newDep);
			return "success";
		} else {
			return "fail";
		}
	}
	
	//부서 삭제
	public void deleteDepartment(Integer departmentId) {
		Optional<Departments> dep = this.departmentRepository.findById(departmentId);
		Departments depTemp = this.departmentRepository.findByDepartmentName("temp").get();
		Positions p = this.pRepo.findByPositionNameAndDepartment("worker", depTemp).get();
		if(dep.isPresent()) {
			List<Users> _uList = this.uRepo.findByDep(dep.get().getDepartmentName());
			for(Users u : _uList) {
				u.setPosition(p);
				this.uRepo.save(u);
			}
			this.pService.deletePositionOfDep(dep.get());
			this.departmentRepository.delete(dep.get());
		}
		
	}
}
