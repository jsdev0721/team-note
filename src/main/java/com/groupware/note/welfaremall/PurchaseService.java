package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {
	private final PurchaseRepository purchaseRepository;
	
	public Purchase save(Purchase purchase) {
		purchase.setPurchaseDate(LocalDateTime.now());
		return this.purchaseRepository.save(purchase);
	}
	public Purchase findByUserAndPurchaseType(Users user , String purchaseType) {
		Optional<Purchase> _purchase = this.purchaseRepository.findByUserAndPurchaseType(user, purchaseType);
		if(_purchase.isEmpty()) {
			return new Purchase();
		}
		return _purchase.get();
	}
}
