package com.groupware.note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.files.FileRepository;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.position.PositionRepository;
import com.groupware.note.position.Positions;

@SpringBootTest
class NoteApplicationTests {
	@Autowired
	private PositionRepository positionsRepository;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	
	
	@Test
	void testJpa() {
		
		String[] Departmentset = {"HR", "accounting", "Marketing", "temp"};
		for(String department : Departmentset) {
			Departments dep = new Departments();
			dep.setDepartmentName(department);
			dep.setPoints(0);
			this.departmentRepository.save(dep);
		}
		
		
		String[] positionset = {"section chief", "deputy", "worker" , "intern"};
		
		for(String position : positionset) {
			for(String dep1 : Departmentset) {
				Positions pos = new Positions();
				pos.setDepartment(departmentService.findBydepartmentName(dep1));
				pos.setPositionName(position);
				positionsRepository.save(pos);
			}
		}
		Files file = new Files();
		file.setOriginFileName("user.png");
		file.setStoreFileName("user.png");
		this.fileRepository.save(file);
		
		//게시판 데이터 TEST용
//		for(int i=1 ; i<=50 ; i++) {
//			Approval approval = new Approval();
//			String content = String.valueOf(i);
//			String title = String.valueOf(i);
//			approval.setUpdateTime(LocalDateTime.now());
//			approval.setContent(content);
//			approval.setTitle(title);
//			approval.setStatus("queue");
//			Users user = this.userService.getUser("admin");
//			approval.setDepartment(user.getPosition().getDepartment());
//			approval.setUser(user);
//			this.approvalService.save(approval);
//		}
	}
}
