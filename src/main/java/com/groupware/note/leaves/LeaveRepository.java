package com.groupware.note.leaves;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface LeaveRepository extends JpaRepository<Leaves, Integer> {
	List<Leaves> findByUser(Users user);
}
