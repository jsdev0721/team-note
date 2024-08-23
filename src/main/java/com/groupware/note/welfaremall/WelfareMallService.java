package com.groupware.note.welfaremall;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WelfareMallService {
	private final WelfareMallRepository welfareMallRepository;
	
	public Pageable getPageable(int page,int quantity) {
		List<Sort.Order>sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("productId"));
		return  PageRequest.of(page, quantity, Sort.by(sorts));
	}
	public Page<WelfareMall> findAll(String type , int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.welfareMallRepository.findByType(type, pageable);
	}
	public Page<WelfareMall> findByProductNameLike(String _productName , String type , int page , int quantity) {
		String productName = "%"+_productName+"%";
		Pageable pageable = getPageable(page, quantity);
		return this.welfareMallRepository.findByTypeAndProductNameLike(type, productName, pageable);
	}
	public List<WelfareMall> findByProductNameLike(String _productName , String type) {
		String productName = "%"+_productName+"%";
		return this.welfareMallRepository.findByTypeAndProductNameLike(productName, _productName);
	}
	public WelfareMall save(WelfareMall welfareMall) {
		return this.welfareMallRepository.save(welfareMall);
	}
	public WelfareMall findById(Integer id) {
		Optional<WelfareMall> _welfareMall = this.welfareMallRepository.findById(id);
		if(_welfareMall.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _welfareMall.get();
	}
	public void delete(WelfareMall welfareMall) {
		this.welfareMallRepository.delete(welfareMall);
	}
}
