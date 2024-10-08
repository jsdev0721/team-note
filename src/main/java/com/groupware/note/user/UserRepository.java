package com.groupware.note.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.groupware.note.position.Positions;




@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{
	
	Optional<Users> findByUsername(String username);
	Optional<Users> findByUserId(Users userId);
	
	@Query("select u from Users u where u.position.department.departmentName = :depName")
	List<Users> findByDep(@Param("depName") String depName);
	
	List<Users> findAllByPosition(Positions position);

	List<Users> findByStatus(String status); //0909 장진수
}
