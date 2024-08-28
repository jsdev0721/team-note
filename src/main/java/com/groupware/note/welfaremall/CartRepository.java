package com.groupware.note.welfaremall;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	List<Cart> findByUserAndType(Users user , String type);
	Optional<Cart> findByProductAndUser(WelfareMall product , Users user);
	Optional<Cart> findByUserAndProduct(Users user , WelfareMall product);
	List<Cart> findByUser(Users user);
}
