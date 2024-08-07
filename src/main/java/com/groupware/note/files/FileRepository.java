package com.groupware.note.files;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<Files, Integer> {
	
	List<Files> findByStoreFileName(String storeFileName);
}
