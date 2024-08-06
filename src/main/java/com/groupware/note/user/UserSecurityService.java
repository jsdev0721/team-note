package com.groupware.note.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.groupware.note.department.Departments;
import com.groupware.note.position.Positions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
	
	private final UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> _users = this.userRepository.findByUsername(username); //'_'는 임시라는 뜻
		
		if(_users.isEmpty()) { //데이터가 없다면
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		Users users = _users.get(); //데이터가 존재하기 때문에 인증 성공
		System.out.println(users.getUsername() + "\t" + users.getPassword());
		List<GrantedAuthority> authorities = new ArrayList<>(); //권한 부여를 해주는 클래스를 List타입의 객체로 생성
		//GrantedAuthority => 권한 부여를 해주는 클래스 (부여할 권한이 여러개일 수 있기 때문에 List에 넣어서 권한을 부여함)
		
		String departmentName = null;
		
		if(users.getPosition() != null) {
			departmentName = users.getPosition().getDepartment().getDepartmentName();
		}
		
		if("admin".equals(username)) { //관리자
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue())); //UserRole에 있는 ADMIN 상수의 값을 부여함
			//SimpleGrantedAuthority => 권한 부여하는 클래스 (해당 클래스의 매개변수로 문자열 값을 하나만 넣을 수 있음)
		}else if("hr".equals(departmentName)) { //인사
			authorities.add(new SimpleGrantedAuthority(UserRole.HR.getValue()));
			System.out.println("aaa"); //권한 부여 확인
		}else if("accounting".equals(departmentName)) { //회계
			authorities.add(new SimpleGrantedAuthority(UserRole.ACCOUNTING.getValue()));
			System.out.println("bbb");
		}else if("marketing".equals(departmentName)) { //영업
			authorities.add(new SimpleGrantedAuthority(UserRole.MARKETING.getValue()));
			System.out.println("ccc");
		}else { //사용자
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
			System.out.println("ddd");
		}
		
		return new User(users.getUsername(), users.getPassword(), authorities); //인증 정보와 인가(권한)을 함께 User()를 통해 넘겨줌
	}

}
