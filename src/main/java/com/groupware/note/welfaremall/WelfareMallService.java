package com.groupware.note.welfaremall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WelfareMallService {
	private final WelfareMallRepository welfareMallRepository;
	
	public Pageable getPageable(int page,int quantity) {
		List<Sort.Order>sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("updateTime"));
		return  PageRequest.of(page, quantity, Sort.by(sorts));
	}
	public Page<WelfareMall> findAll(int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.welfareMallRepository.findAll(pageable);
	}
	public Page<WelfareMall> findByProductName(String productName , int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.welfareMallRepository.findByProductName(productName , pageable);
	}
	public WelfareMall save(WelfareMall welfareMall) {
		return this.welfareMallRepository.save(welfareMall);
	}
}
