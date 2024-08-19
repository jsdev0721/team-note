package com.groupware.note.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupware.note.position.Positions;
import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{
	
	Optional<Users> findByUsername(String username);
	Optional<Users> findByUserId(Users userId);
	
	

}
