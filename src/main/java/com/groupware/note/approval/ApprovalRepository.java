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
	Page<Approval> findByDepartmentAndStatus(Departments department , String status , Pageable pageable);
	Page<Approval> findByUser(Users user ,Pageable pageable);
	Page<Approval> findByDepartmentAndStatusAndTitleLike(Departments department , String status , String title , Pageable pageable);
	
	Page<Approval> findByUserAndStatus(Users users, String status, Pageable pageable);
	Page<Approval> findByUserAndStatusAndTitleLike(Users users, String status , String title , Pageable pageable);
	
	List<Approval> findByUser(Users user);

	List<Approval> findByUserAndStatus(Users users, String status);

}
