package com.groupware.note.welfaremall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	List<Cart> findByUserAndType(Users user , String type);
	List<Cart> findByUserAndTypeAndStatus(Users user , String type , String status);
	Optional<Cart> findByProductAndUser(WelfareMall product , Users user);
	Optional<Cart> findByUserAndProductAndStatusAndProductOptionLike(Users user , WelfareMall product , String status , String productOption);
	List<Cart> findByUser(Users user);
	List<Cart> findByUserAndTypeAndStatusNotOrderByAddDate(Users user , String type , String status);
	List<Cart> findByUserAndTypeAndStatusAndAddDate(Users user , String type , String status , LocalDateTime addDate);
	List<Cart> findByProduct(WelfareMall product);
	
	//회계
	@Query("select c from Cart c where c.status= :status and c.type= :type and c.user.position.department= :department order by c.addDate desc")
	List<Cart> findByStatusAndTypeAndDepOrderByDate(@Param("status") String status,  @Param("type") String type,  @Param("department") Departments department );
	
	@Query("select c from Cart c where c.status= :status and c.type= :type and c.user = :user order by c.addDate desc")
	List<Cart> findByStatusAndTypeAndUserOrderByDate(@Param("status") String status,  @Param("type") String type,  @Param("user") Users user );
	
	List<Cart> findByStatusAndTypeAndUserAndAddDateBetween(String status, String type, Users user, LocalDateTime start, LocalDateTime end);
	
	@Query("select c FROM Cart c WHERE c.type= :type and c.status= :status and c.user.position.department= :department and c.addDate > :start AND c.addDate <= :end order by c.addDate desc")
	List<Cart> findByStatusAndTypeAndDepAndDateBetOrderBydate(@Param("status") String status, @Param("type") String type, @Param("department") Departments department, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	
	
}
