package com.groupware.note.form;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Forms, Integer>{
	
	Page<Forms> findAll(Pageable pageable);
	Page<Forms> findByTitleLike(Pageable pageable,String searchKeyword);
	
		

}
