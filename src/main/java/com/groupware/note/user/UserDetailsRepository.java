package com.groupware.note.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.groupware.note.department.Departments;

import java.util.List;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
	
	Optional<UserDetails> findByEmail(String email);
	Optional<UserDetails> findByName(String name);
	List<UserDetails> findByNameLike(String name);
	Optional<UserDetails> findByUser(Users user);
	
	@Query("select u from UserDetails u where u.user.position.department = :department order by u.user.position.positionId , u.name")
	List<UserDetails> findByDepOrderByPositionAndName(@Param("department") Departments department );
}
