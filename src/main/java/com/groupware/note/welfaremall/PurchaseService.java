package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
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
	public Purchase findById(Integer id) {
		Optional<Purchase> _purchase = this.purchaseRepository.findById(id);
		if(_purchase.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _purchase.get();
	}
	public void delete(Purchase purchase) {
		this.purchaseRepository.delete(purchase);
	}
	@Scheduled(cron = "0 0 0 1 * *")
	public void updateProcess() {
		for(Purchase purchase : findAll()) {
			if(purchase.getPurchaseStatus().equals("process")) {
				purchase.setPurchaseStatus("complete");
				this.purchaseRepository.save(purchase);
			}
		}
	}
	public List<Purchase> findAll() {
		return this.purchaseRepository.findAll();
	}
}
