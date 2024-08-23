package com.groupware.note.welfaremall;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelfareMallRepository extends JpaRepository<WelfareMall, Integer> {
	Page<WelfareMall> findByType(String type , Pageable pageable);
	Page<WelfareMall> findByTypeAndProductNameLike(String type , String productName , Pageable pageable);
	List<WelfareMall> findByTypeAndProductNameLike(String type , String productName);
}
