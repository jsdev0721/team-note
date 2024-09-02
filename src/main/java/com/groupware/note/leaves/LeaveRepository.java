package com.groupware.note.leaves;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.attendance.Attendance;
import com.groupware.note.user.Users;

@Repository
public interface LeaveRepository extends JpaRepository<Leaves, Integer> {
	Optional<Leaves> findByUser(Users user);

}
