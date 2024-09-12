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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
	
	private final UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> _users = this.userRepository.findByUsername(username);
		
		if(_users.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		Users users = _users.get();
		System.out.println(users.getUsername() + "\t" + users.getPassword());
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		String departmentName = null;
		String position = null;
		if(users.getPosition() != null) {
			departmentName = users.getPosition().getDepartment().getDepartmentName();
			position = users.getPosition().getPositionName();
		}
		
		if("admin".equals(username)) { //관리자
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
			authorities.add(new SimpleGrantedAuthority(UserRole.HR.getValue()));
			authorities.add(new SimpleGrantedAuthority(UserRole.ACCOUNTING.getValue()));
			authorities.add(new SimpleGrantedAuthority(UserRole.MARKETING.getValue()));
			authorities.add(new SimpleGrantedAuthority(UserRole.SECTIONCHEIF.getValue()));
		}else if("HR".equals(departmentName)) { //인사
			authorities.add(new SimpleGrantedAuthority(UserRole.HR.getValue()));
		}else if("accounting".equals(departmentName)) { //회계
			authorities.add(new SimpleGrantedAuthority(UserRole.ACCOUNTING.getValue()));
		}else if("Marketing".equals(departmentName)) { //영업
			authorities.add(new SimpleGrantedAuthority(UserRole.MARKETING.getValue()));
		}else { //사용자
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		if("section chief".equals(position)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.SECTIONCHEIF.getValue()));
		}
		else if("deputy".equals(position)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.DEPUTY.getValue()));
		}
		else if("worker".equals(position)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.WORKER.getValue()));
		}
		else {
			authorities.add(new SimpleGrantedAuthority(UserRole.INTERN.getValue()));
		}
		return new User(users.getUsername(), users.getPassword(), authorities);
	}

}
