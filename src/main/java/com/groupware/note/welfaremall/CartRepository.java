package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.user.Users;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	List<Cart> findByUserAndType(Users user , String type);
	List<Cart> findByUserAndTypeAndStatus(Users user , String type , String status);
	Optional<Cart> findByProductAndUser(WelfareMall product , Users user);
	Optional<Cart> findByUserAndProductAndStatusAndOptionLike(Users user , WelfareMall product , String status , String option);
	List<Cart> findByUser(Users user);
	List<Cart> findByUserAndTypeAndStatusNotOrderByAddDate(Users user , String type , String status);
	List<Cart> findByUserAndTypeAndStatusAndAddDate(Users user , String type , String status , LocalDateTime addDate);
}
