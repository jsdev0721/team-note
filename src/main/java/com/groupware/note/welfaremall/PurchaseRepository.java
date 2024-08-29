package com.groupware.note.welfaremall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

	//Optional<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
	List<Purchase> findByUser(Users users);

	List<Purchase> findByUserAndPurchaseTypeAndPurchaseStatus(Users user , String purchaseType , String purchaseStatus);
	List<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
	
	
	//비용처리에서 가져가는용
	List<Purchase> findByPurchaseStatus(String purchaseStatus);
	List<Purchase> findByPurchaseStatusAndPurchaseType(String purchaseStatus, String purchaseType);

}
