package com.groupware.note.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.files.Files;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormService {
	
	private final FormRepository formRepository;
	
	public Page<Forms> formsList(int page) {
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
		
		return this.formRepository.findAll(pageable);
	}
	public Page<Forms> SearchList(int page,String searchKeyword) {
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
		
		return this.formRepository.findByTitleLike(pageable, "%"+searchKeyword+"%");
	}
	public Forms getForm(Integer formId) {
		Optional<Forms> form = this.formRepository.findById(formId);
		if(form.isPresent()) {
			 return form.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		 
	}
	public void create(String title, String content,Users user,List<Files> files) {
		Forms form = new Forms();
		form.setTitle(title);
		form.setContent(content);
		form.setFileList(files);
		form.setUser(user);
		form.setCreateDate(LocalDateTime.now());
		this.formRepository.save(form);
		
	}

}
