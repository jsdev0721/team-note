package com.groupware.note.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

	
	@Query("SELECT a FROM Attendance a WHERE a.user.username = :username AND a.checkOutTime IS NULL ORDER BY a.checkInTime DESC")
	Attendance findLatestCheckInByUser(@Param("username") String username);
	
	@Query("SELECT a FROM Attendance a WHERE a.user.userId = :userId")
	List<Attendance> findByUserId(@Param("userId") Integer userId);
}
