package com.groupware.note.notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.groupware.note.DataNotFoundException;
import com.groupware.note.files.FileRepository;
import com.groupware.note.files.FileService;
import com.groupware.note.files.Files;
import com.groupware.note.user.Users;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

	private final NoticesRepository noticesRepository;
	private final FileRepository fileRepository;
	
	public Pageable getPageable(int page , int quantity) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		return PageRequest.of(page, quantity, Sort.by(sorts));
	}
	public Page<Notices> noticesList(int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.noticesRepository.findAll(pageable);
	}
	public Page<Notices> noticesSearchList(int page, String searchkeyword) {
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
		
		return this.noticesRepository.findByTitleLike("%"+searchkeyword+"%", pageable);
	}
	public Notices getNotice(Integer noticeId) {
		Optional<Notices> notices = this.noticesRepository.findById(noticeId);
		if(notices.isPresent()) {
			return notices.get();
		}else {
		throw new DataNotFoundException("데이터가 없습니다");
			}
	}
	public void create(String title,String content, Users users,List<Files> fileList) {//공지사항 작성날짜
		
		Notices notices = new Notices();
		notices.setTitle(title);
		notices.setContent(content);
		notices.setUser(users);
		notices.setFileList(fileList);
		notices.setCreateDate(LocalDateTime.now());
		this.noticesRepository.save(notices);
		
	}
	public void updateNotice(Notices notices, String title,String content,Users users,List<Files> fileList) {
		
		notices.setTitle(title);
		notices.setContent(content);
		notices.setUser(users);
		notices.setFileList(fileList);
		notices.setCreateDate(LocalDateTime.now());
		this.noticesRepository.save(notices);
	}
	public void deleteNotices(Notices notices) {
		  this.noticesRepository.delete(notices);
		//return this.noticesRepository.deleteById(noticesId);
	}
	
	
		
}
	
	
		
	

