package com.groupware.note.expense;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.groupware.note.welfaremall.Purchase;
import com.groupware.note.welfaremall.PurchaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseDataService {
	private final PurchaseRepository pRepo;
	//전체 데이터
	public List<Purchase> basicList(){
		List<Purchase> list = new ArrayList<>();
		list = this.pRepo.findByPurchaseStatus("complete");
		return list;
	}
	
	//부서별/사람별 데이터
	public List<Purchase> ListByType(String purchaseType) {
		List<Purchase> list = new ArrayList<>();
		list = this.pRepo.findByPurchaseStatusAndPurchaseType("complete", purchaseType);
		return list;
	}
}
 //포인트 입력기능 만들기