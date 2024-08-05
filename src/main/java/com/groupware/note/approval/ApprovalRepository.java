package com.groupware.note.approval;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
	List<Approval> findByDepartment(Departments department , Pageable pageable);
	Page<Approval> findByUser(Users user , Pageable pageable);
}
