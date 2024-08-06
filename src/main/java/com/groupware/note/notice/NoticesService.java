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
import com.groupware.note.DataNotFoundException;
import com.groupware.note.user.Users;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

	private final NoticesRepository noticesRepository;
	
	public Page<Notices> noticesList(int page) {
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		Pageable pageable = PageRequest.of(page, 10,Sort.by(sorts));
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
	
	public void create(String title,String content,String attachment,Users users) {//공지사항 작성날짜
		Notices notices = new Notices();
		notices.setTitle(title);
		notices.setContent(content);
		notices.setAttachment(attachment);
		notices.setUser(users);
		notices.setCreateDate(LocalDateTime.now());
		this.noticesRepository.save(notices);
		
	}
	/*public List<Notices> search(String searchkeyword){
		List<Notices> noticesList = noticesRepository.findByTitleContaining(searchkeyword);
		return this.noticesRepository.findByTitleLike(searchkeyword)
	}*/
	
	
		
	
}
