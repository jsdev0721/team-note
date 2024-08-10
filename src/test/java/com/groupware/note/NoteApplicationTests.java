package com.groupware.note;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.groupware.note.approval.Approval;
import com.groupware.note.approval.ApprovalService;
import com.groupware.note.department.DepartmentRepository;
import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.files.FileRepository;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.form.FormService;
import com.groupware.note.notice.NoticesService;
import com.groupware.note.position.PositionRepository;
import com.groupware.note.position.Positions;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsRepository;
import com.groupware.note.user.UserDetailsService;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

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
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private NoticesService noticesService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private UserDetailsRepository userDetailsRepository; 
	
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
		Files _file = new Files();
		_file.setOriginFileName("user.png");
		_file.setStoreFileName("user.png");
		this.fileRepository.save(_file);
		//아이디 비밀번호 temp
		//로그인은 신규 가입으로 진행할 것
		this.userService.create("temp", "temp");

			Users user = this.userService.getUser("temp");
			Files file = this.fileService.findByFiles(1);
//			this.userDetailsService.create(user, "temp", LocalDate.now(), "temp@temp", file);
		//결재게시판 데이터 TEST용
		for(int i=1 ; i<=50 ; i++) {
			Approval approval = new Approval();
			String content = String.valueOf(i);
			String title = String.valueOf(i);
			approval.setUpdateTime(LocalDateTime.now());
			approval.setContent(content);
			approval.setTitle(title);
			approval.setStatus("queue");
			approval.setDepartment(user.getPosition().getDepartment());
			approval.setUser(user);
			this.approvalService.save(approval);
		}
		//공지사항게시판 데이터 TEST용
		for(int i=1 ; i<=50 ; i++) {
			String title = String.valueOf(i);
			String content = String.valueOf(i);
			this.noticesService.create(title, content, user, null);
		}
		//서식게시판 데이터 TEST용
		for(int i=1 ; i<=50 ; i++) {
			String title = String.valueOf(i);
			String content = String.valueOf(i);
			this.formService.create(title, content, user, null);
		}
	}
}
