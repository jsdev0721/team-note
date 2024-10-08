package com.groupware.note.user;

import java.net.MalformedURLException;

import java.security.Principal;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.approval.ApprovalService;
import com.groupware.note.attendance.Attendance;
import com.groupware.note.attendance.AttendanceService;
import com.groupware.note.calendar.CalendarService;
import com.groupware.note.department.DepartmentService;
import com.groupware.note.department.Departments;
import com.groupware.note.email.EmailService;
import com.groupware.note.expense.ExpenseDataService;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.form.FormService;
import com.groupware.note.leaves.LeaveService;
import com.groupware.note.leaves.Leaves;
import com.groupware.note.message.chatRoomService;
import com.groupware.note.notice.NoticesService;
import com.groupware.note.position.PositionService;
import com.groupware.note.position.Positions;
import com.groupware.note.position.UpdateUserPositions;
import com.groupware.note.position.UpdateUserPositionsService;
import com.groupware.note.welfaremall.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final UserDetailsService userDetailsService;
	private final EmailService emailService;
	private final FileService fileService;
	private final AttendanceService attendanceService;
	private final PositionService positionService;
	private final ApprovalService approvalService;
	private final NoticesService noticesService;
	private final FormService formService;
	private final CalendarService calendarService;
	private final ExpenseDataService expenseDataService;
	private final LeaveService leaveService;
	private final CartService cartService;
	private final chatRoomService chatRoomService;
	//private final MessageService messageService;
	private final DepartmentService departmentsService;
	private final UpdateUserPositionsService updateUserPositionsService;
	
	@GetMapping("/login")
	public String login(Principal principal) { // 0809 장진수 : 로그인 상태에서도 login.html 에 들어갈 수 있길래, 구분해둠
		if(principal != null) {
			Users user = this.userService.getUser(principal.getName());
			if(!user.getStatus().equals("출근")) {
				return "attendance/attendanceButton";
			}else {				
				return "redirect:/";
			}
		}else {			
			return "user/login";
		}
	}
	
	@GetMapping("/regist")
	public String regist(UserCreateForm userCreateForm) {
		return "user/regist";
	}
	
	@PostMapping("/regist")
	public String regist(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "user/regist";
		}
		if(!userCreateForm.getPassword().equals(userCreateForm.getPasswordCheck())) {
			bindingResult.rejectValue("passwordCheck", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "user/regist";
		}
		try {
			Users users = this.userService.create(userCreateForm.getUsername(), userCreateForm.getPassword());
			this.userDetailsService.create(users, userCreateForm.getName(), userCreateForm.getBirthdate(), userCreateForm.getEmail(),  15);
		} catch (DataIntegrityViolationException e) { 
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "user/regist";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "user/regist";
		}
		
		return "user/login";
	}
	
	@GetMapping("/find/id")
	public String findID() {
		return "user/findID";
	}
	
	@GetMapping("/find/pw")
	public String findPW(UserPasswordForm userPasswordForm) {
		return "user/findPW";
	}
	
	@ResponseBody
	@PostMapping("/find/pw")
	public String findPW(@RequestParam(value = "username") String username) {
		Users users = this.userService.getUser(username);
		UserDetails userDetails = this.userDetailsService.findByUser(users);
    	int domain = userDetails.getEmail().indexOf("@");
		String domainCheck = userDetails.getEmail().substring(domain+1);
		if(domainCheck.equals("naver.com") || domainCheck.equals("daum.net") || domainCheck.equals("gmail.com")
				|| domainCheck.equals("icloud.com") || domainCheck.equals("kakao.com") || domainCheck.equals("nate.com")) {
			
			int number = emailService.sendMail(userDetails.getEmail());
			String code = "" + number;
			return code;
		}else {
			throw new DataNotFoundException("데이터를 찾을 수 없습니다");
		}
	}
	
	@PostMapping("/change/pw")
	public String changePW(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			Users users = this.userService.findPW(userPasswordForm.getUsername());
			Boolean check = true;
			model.addAttribute("check", check);
			model.addAttribute("users", users);
			return "user/findPW";
		}
		if(!userPasswordForm.getPassword().equals(userPasswordForm.getPasswordCheck())) {
			bindingResult.rejectValue("passwordCheck", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			Users users = this.userService.findPW(userPasswordForm.getUsername());
			Boolean check = true;
			model.addAttribute("check", check);
			model.addAttribute("users", users);
			return "user/findPW";
		}
		this.userService.changePW(userPasswordForm.getUsername(), userPasswordForm.getPassword());
		return "redirect:/user/login";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/photo")
	public String photoUpdate(@RequestParam(value = "multipartFiles") MultipartFile multipartFile, Principal principal) {
		try {
			Users users = this.userService.getUser(principal.getName());
			UserDetails userDetails = this.userDetailsService.findByUser(users);
			if(userDetails.getPhoto() != null) {
				Files photo = userDetails.getPhoto();
				this.userDetailsService.deletePhoto(userDetails);
				this.fileService.delete(photo);
			}		
			Files files = this.fileService.uploadFile(multipartFile);
			this.userDetailsService.uploadPhoto(users, files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/photo/{id}")
	public ResponseEntity<Resource> photo(@PathVariable("id") Integer id) throws MalformedURLException{
		Files file = this.fileService.findByFiles(id);
		return this.fileService.photoView(file);
	}
	
	@GetMapping("/list")
	public String userList(Model model,SearchListForm searchListForm) {
		List<UserDetails> userList = this.userDetailsService.userfindByAll();
		List<UpdateUserPositions> positionsList =this.updateUserPositionsService.userfindByAll();
		model.addAttribute("userList",userList);
		model.addAttribute("positionsList",positionsList);
		
		return "HR/HR_list";
		
	}
	@GetMapping("/detail/{userId}")//detail에 대한 고유값
	public String getUser(Model model,@PathVariable("userId") Integer userId) {
		Users users = this.userService.getUser(userId);
		Positions positions = users.getPosition();
		UserDetails userDetails = this.userDetailsService.getUser(userId);
		List<Attendance> attendance = this.attendanceService.findById(userId);
		List<Leaves> leaves = this.leaveService.findByUser(users);
		model.addAttribute("users", users);
		model.addAttribute("positions",positions);
		model.addAttribute("userDetails",userDetails);
		model.addAttribute("attendance", attendance);
		model.addAttribute("leaves", leaves);
		System.out.println("불러와 주우우웅우우우세야야야양야ㅑㅇ");
		
		return "HR/HR_detail";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{userId}")
	public String userUpdate(Model model,@PathVariable("userId") Integer userId) {
		Users users =this.userService.getUser(userId);
		model.addAttribute("users",users);
		model.addAttribute("depList",  this.departmentsService.findAll());
		
		return "HR/HR_update";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{userId}")
	public String userupdate(@PathVariable("userId") Integer userId,@RequestParam(value="positionName")String positionName  
			,@RequestParam(value="departmentId")String departmentName
			,@RequestParam(value="updateTime")LocalDateTime localDateTime) {

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(userId);
		System.out.println(positionName);
		Departments departments=this.departmentsService.findBydepartmentName(departmentName);
		Positions positions= this.positionService.findByPositionNameAndDepartment(positionName, departments);
		
		LocalDateTime now = LocalDateTime.now();
		
		if(localDateTime.isAfter(now)) {
			this.positionService.updatePosition(userId, positions, localDateTime);
			System.out.println("부서/직급 예약 변경완료");
		}else {
			this.positionService.updatePosition(userId, positions);
			System.out.println("부서/직급 즉시변경완료");
		}
		return "redirect:/user/list";
	}
	
	@PostMapping("/list")
	public String userSearchList(Model model,@Valid SearchListForm searchListForm) {
		List<UserDetails> userList = this.userDetailsService.searchList(searchListForm.getSearchKeyword());
		System.out.println(searchListForm.getSearchKeyword());
		model.addAttribute("userList",userList);
		System.out.println("검색을 합니다");
		
		return "HR/HR_list";
	}
	
	@GetMapping("/delete/{userId}")
	public String userDelete(@PathVariable(value="userId") Integer userId,Principal principal){
		
		Users users =this.userService.getUser(userId);
		UserDetails userDetails= this.userDetailsService.getUser(userId);
		if(!users.getUsername().equals(principal.getName())) {
		try {
			this.expenseDataService.deleteExpense(userDetails);
			this.attendanceService.deleteAttendance(users);
			this.noticesService.deleteNotices(users);
			this.formService.deleteForm(users);
			this.calendarService.deleteCalender(users);
			this.approvalService.deleteApproval(users);
			this.leaveService.deleteLeave(users);
			this.cartService.deleteCart(users);
			this.chatRoomService.deleteChatRoom(users);
			this.userDetailsService.deleteUserDetails(users);
			this.userService.deletePosition(userId);
			this.userService.deleteUser(userId);
		}catch(DataNotFoundException e) {
			e.printStackTrace();
			System.out.println("처리할 데이터가 없습니다");
			}
		}else {return "redirect:/user/list";}
		System.out.println("내 계정은 삭제할수 없습니다");
		
		return "redirect:/user/list";
	}
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable(value = "id")Integer id) {
		Files file = this.fileService.findByFiles(id);
		return this.fileService.downloadFile(file);
	}
	
}
