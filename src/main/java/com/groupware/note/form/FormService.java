package com.groupware.note.form;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormService {
	
	private final FormRepository formRepository;
	
	public List<Forms> formList(){
		return this.formRepository.findAll();
		
	}
	public Forms getForm(Integer formsId) {
		Optional<Forms> forms = this.formRepository.findById(formsId);
		if(forms.isPresent()) {
			return forms.get();
		}else {
		throw new DataNotFoundException("데이터가 없습니다");
			}
	}
	
	public void create(String title,String content,String attachment,Users users) {//공지사항 작성날짜
		Forms form = new Forms();
		form.setTitle(title);
		form.setContent(content);
		form.setAttachment(attachment);
		form.setUser(users);
		form.setCreateDate(LocalDateTime.now());
		this.formRepository.save(form);
		
	
	
	
	
	
	}
}
