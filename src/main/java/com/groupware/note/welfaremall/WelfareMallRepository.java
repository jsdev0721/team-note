package com.groupware.note.welfaremall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelfareMallRepository extends JpaRepository<WelfareMall, Integer> {
	Page<WelfareMall> findByProductName(String productName , Pageable pageable);
}
