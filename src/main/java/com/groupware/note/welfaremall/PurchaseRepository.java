package com.groupware.note.welfaremall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
	List<Purchase> findByUserAndPurchaseTypeAndPurchaseStatus(Users user , String purchaseType , String purchaseStatus);
	List<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
}
