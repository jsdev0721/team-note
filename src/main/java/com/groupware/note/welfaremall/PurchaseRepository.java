package com.groupware.note.welfaremall;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
	Optional<Purchase> findByUserAndPurchaseType(Users user , String purchaseType);
	List<Purchase> findByUser(Users users);
}
