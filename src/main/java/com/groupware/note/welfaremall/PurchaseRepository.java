package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

	//Optional<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
	List<Purchase> findByUser(Users users);

	List<Purchase> findByUserAndPurchaseTypeAndPurchaseStatus(Users user , String purchaseType , String purchaseStatus);
	List<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
	
	
	//비용처리에서 가져가는용
	List<Purchase> findByPurchaseStatus(String purchaseStatus);
	List<Purchase> findByPurchaseStatusAndPurchaseTypeAndDepartment(String purchaseStatus, String purchaseType, Departments department);
	List<Purchase> findByPurchaseStatusAndPurchaseTypeAndUser(String purchaseStatus, String purchaseType, Users user);
	List<Purchase> findByPurchaseStatusAndPurchaseTypeAndUserAndPurchaseDateBetween(String purchaseStatus, String purchaseType, Users user, LocalDateTime start, LocalDateTime end);
	List<Purchase> findByPurchaseStatusAndPurchaseTypeAndDepartmentAndPurchaseDateBetween(String purchaseStatus, String purchaseType, Departments department, LocalDateTime start, LocalDateTime end);
}
