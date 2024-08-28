package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	
	public List<Cart> findByUser(Users user , String type) {
		return this.cartRepository.findByUserAndType(user, type);
	}
	public Cart findByProductAndUser(WelfareMall welfareMall , Users user) {
		Optional<Cart> _cart = this.cartRepository.findByProductAndUser(welfareMall , user);
		if(_cart.isEmpty()) {
			return new Cart();
		}
		return _cart.get();
	}
	public Cart save(Cart cart) {
		cart.setAddDate(LocalDateTime.now());
		return this.cartRepository.save(cart);
	}
	public Cart findById(Integer id) {
		Optional<Cart> _cart = this.cartRepository.findById(id);
		if(_cart.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _cart.get();
	}
	public void delete(Cart cart) {
		this.cartRepository.delete(cart);
	}
	public void deleteCart(Users users) {
		List<Cart> list=this.cartRepository.findByUser(users);
		if(!list.isEmpty() || list.isEmpty()) {
			for(Cart cart : list) {
				cart.setUser(null);
				this.cartRepository.save(cart);
				}
			}else {throw new DataNotFoundException("데이터가 없습니다");}
		
	}
	
}
