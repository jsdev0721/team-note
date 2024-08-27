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
	
	
	public Pageable getPageable(int page , int quantity) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		return PageRequest.of(page, quantity, Sort.by(sorts));
	}
	public Page<Forms> formsList(int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.formRepository.findAll(pageable);
	}
	public Page<Forms> searchList(int page,String searchKeyword) {
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
		
		return this.formRepository.findByTitleLike(pageable, "%"+searchKeyword+"%");
	}
	public Forms getForm(Integer formId) {
		Optional<Forms> form = this.formRepository.findById(formId);
		if(form.isPresent()) {
			 return form.get();
		}else {throw new DataNotFoundException("데이터가 없습니다");}
		 
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
	public void update(Forms forms,String title, String content,Users user,List<Files> files) {
		
		forms.setTitle(title);
		forms.setContent(content);
		forms.setUser(user);
		forms.setFileList(files);
		forms.setCreateDate(LocalDateTime.now());
		this.formRepository.save(forms);
	}
	public void delete(Forms forms) {
		 this.formRepository.delete(forms);	
	}
	public void deleteForm(Users user) {
		List<Forms> list = this.formRepository.findByUser(user);
		if(!list.isEmpty()) {
			for(Forms forms : list) {
				forms.setUser(null);
				this.formRepository.save(forms);
			}
		}else {throw new DataNotFoundException("데이터가 없습니다");}	
	}

}
