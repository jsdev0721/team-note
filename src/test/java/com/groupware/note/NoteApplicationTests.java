package com.groupware.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.DepartmentService;
import com.groupware.note.position.PositionRepository;
import com.groupware.note.user.UserRepository;

@SpringBootTest
class NoteApplicationTests {
	@Autowired
	private PositionRepository positionsRepository;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	void testJpa() {
		
//		String[] Departmentset = {"HR", "accounting", "Marketing", "temp"};
//		for(String department : Departmentset) {
//			Departments dep = new Departments();
//			dep.setDepartmentName(department);
//			dep.setPoints(0);
//			this.departmentRepository.save(dep);
//		}
//		
//		
//		String[] positionset = {"section chief", "deputy", "worker" , "intern"};
//		
//		for(String position : positionset) {
//			for(String dep1 : Departmentset) {
//				Positions pos = new Positions();
//				pos.setDepartment(departmentService.findBydepartmentName(dep1));
//				pos.setPositionName(position);
//				positionsRepository.save(pos);
//			}
//		}
		
		
		
		
		
		
		
		
//		Departments dmHR = new Departments();
//			dmHR.setDepartmentName("HR");
//			dmHR.setPoints(0);
//			this.departmentRepository.save(dmHR);
//		Departments dmAcc = new Departments();
//			dmAcc.setDepartmentName("Accounting");
//			dmAcc.setPoints(0);
//			this.departmentRepository.save(dmAcc);
//		Departments dmMar = new Departments();
//			dmMar.setDepartmentName("Marketing");
//			dmMar.setPoints(0);
//			this.departmentRepository.save(dmMar);
//		Departments dmTemp = new Departments();
//			dmTemp.setDepartmentName("Temp");
//			dmTemp.setPoints(0);
//			this.departmentRepository.save(dmTemp);
//		
		
			
		
		
		
		
	}
	
	
	

}
