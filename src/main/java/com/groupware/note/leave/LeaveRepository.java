package com.groupware.note.leave;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.attendance.Attendance;
import com.groupware.note.user.Users;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
	Optional<Leave> findByUser(Users user);

}
