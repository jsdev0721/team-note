package com.groupware.note.attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groupware.note.user.UserRepository;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	
	public void createIn(String username, double lat, double lon, String reason) {
		Users user = new Users();
		user = this.userService.getUser(username);
		
		Attendance a = new Attendance();
		a.setUser(user);
		a.setInTimeLat(lat);
		a.setInTimeLon(lon);
		a.setCheckInTime(LocalDateTime.now());
		a.setReason(reason);
		this.attendanceRepository.save(a);
		
		user.setStatus("출근");
		this.userRepository.save(user);
	}
	
	public void createOut(String username, double lat, double lon) {
		Users user = new Users();
		user = this.userService.getUser(username);		
		Attendance attendance = this.attendanceRepository.findLatestCheckInByUser(username);
		LocalDateTime checkOutTime = LocalDateTime.now();
		attendance.setCheckOutTime(checkOutTime);
		attendance.setOutTimeLat(lat);
		attendance.setOutTimeLon(lon);
		
		Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime()); // 근무시간 계산
		LocalTime workTime = LocalTime.ofSecondOfDay(duration.getSeconds());
		attendance.setWorkTime(workTime);
		
		LocalDateTime overLineTime = attendance.getCheckInTime().with(LocalTime.of(19, 0, 0)); // 초과시간 계산
		if(checkOutTime.isAfter(overLineTime)) {
			LocalDateTime overDateTime = attendance.getCheckInTime().with(LocalTime.of(19, 0, 0));
			Duration duration2 = Duration.between(overDateTime, attendance.getCheckOutTime());
			LocalTime overTime = LocalTime.ofSecondOfDay(duration2.getSeconds());
			attendance.setOverTime(overTime);			
		}
	
		this.attendanceRepository.save(attendance);
		
		user.setStatus("퇴근"); // 유저 상태 출근으로 변경
		this.userRepository.save(user);
	}
	
	public List<Attendance> getAttendanceAllList(Integer userId){ //유저의 전체 근무기록
		return this.attendanceRepository.findByUserId(userId);
	}
	
	public List<String> getDateList(Integer userId) { // "년도-월별" String 리스트
        List<Attendance> attendances = this.attendanceRepository.findByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");  // 연도-월 설정
        
        // set을 쓴 이유 중복제거 / 빠른 검색 성능!(출근기록 많으니깐)
        Set<String> dateSet = attendances.stream() // 출근기록 리스트를 SET으로 (근태기록은 많으니깐 빠른 set으로)
                .map(a -> a.getCheckInTime().format(formatter))  // 각 출근 기록에서 CheckInTime에서 연도-월로 포맷팅
                .collect(Collectors.toSet());  // 변환된 문자열을 Set으로 수집하여 중복 제거

        return dateSet.stream().sorted().collect(Collectors.toList()); // Set을 List로 변환하여 반환
    }
	
	
	public List<Attendance> getAttendanceList(Integer userId, String date) { // 년도-월별 불러오는 근무기록
		// "2024-08" 형태의 문자열을 파싱하여 YearMonth 객체 생성
	    YearMonth yearMonth = YearMonth.parse(date);

	    // 해당 월의 첫 번째 날과 마지막 날을 구함
	    LocalDate startDate = yearMonth.atDay(1);
	    LocalDate endDate = yearMonth.atEndOfMonth();

	    // 시작과 끝 날짜의 시간을 설정
	    LocalDateTime startOfDay = startDate.atStartOfDay();
	    LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

	    // 주어진 기간 내의 출근 기록을 조회
	    return attendanceRepository.findByUserIdAndCheckInDate(userId, startOfDay, endOfDay);
	
	}
	
	

	
}
