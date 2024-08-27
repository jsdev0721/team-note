package com.groupware.note.form;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.groupware.note.user.Users;


@Repository
public interface FormRepository extends JpaRepository<Forms, Integer>{
	
	Page<Forms> findAll(Pageable pageable);
	Page<Forms> findByTitleLike(Pageable pageable,String searchKeyword);
	List<Forms> findByUser(Users user);
	
		

}
