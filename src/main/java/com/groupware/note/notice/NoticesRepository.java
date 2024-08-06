package com.groupware.note.notice;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticesRepository  extends JpaRepository<Notices, Integer>{
	
	Page<Notices> findByTitleLike(String searchkeyword, Pageable pageable);
	Page<Notices> findAll(Pageable pageable);

}
