package com.groupware.note.expense;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	@Query("select e from Expense e order by e.useDate DESC limit 100")
	List<Expense> findOrderByUseDate();
	
	@Query("select e from Expense e where e.writer.name like :name order by e.useDate desc")
	List<Expense> findByNameOrderByDate(@Param("name") String name );
	
	@Query("select e from Expense e where e.writer.user.position.department.departmentName like :departmentName order by e.useDate desc, e.writer.name asc")
	List<Expense> findByDepOrderByDate(@Param("departmentName") String departmentName );
	
	
	@Query("select e from Expense e where e.writer.email = :email order by e.useDate desc")
	List<Expense> findByEmailOrderByDate(@Param("email") String email); 
	
	@Query("select e from Expense e where e.writer.user.position.department.departmentName = :departmentName order by e.useDate desc, e.writer.name asc ")
	List<Expense> findByDepOrderByDateandName(@Param("departmentName") String departmentName);
	
	@Query("select e from Expense e where e.useDate = :useDate order by e.writer.name desc")
	List<Expense> findByDateOrderBy(@Param("useDate") LocalDateTime date);
	
	@Query("select e from Expense e where e.useDate >= :startDate and e.useDate <= :endDate order by e.useDate desc, e.writer.user.position.department.departmentName desc, e.writer.name desc")
	List<Expense> findBetDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	
}
