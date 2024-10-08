package com.groupware.note.approval;

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
import com.groupware.note.department.Departments;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {
	private final ApprovalRepository approvalRepository;
	
	public Pageable getPageable(int page,int quantity) {
		List<Sort.Order>sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("updateTime"));
		return  PageRequest.of(page, quantity, Sort.by(sorts));
	}
	public Page<Approval> ApprovalList(Users users, Departments department , String status , int page , int quantity) {
		Pageable pageable = getPageable(page , quantity);
		if(status.equals("queue")) {
			return this.approvalRepository.findByUserAndStatus(users, status, pageable);
		}
		return this.approvalRepository.findByDepartmentAndStatus(department, status, pageable);
	}
	
	public Page<Approval> ApprovalfindbyUser(Users user , int page , int quantity) {
		Pageable pageable = getPageable(page , quantity);
		return this.approvalRepository.findByUser(user , pageable);
	}
	
	public Approval save(Approval approval) {
		if(approval.getApprovalId()==null) {
			approval.setStatus("queue");
		}
		approval.setUpdateTime(LocalDateTime.now());
		this.approvalRepository.save(approval);
		return approval;
	}
	
	public Approval findById(Integer id) {
		Optional<Approval> _approval = this.approvalRepository.findById(id);
		if(_approval.isEmpty()) {
			throw new DataNotFoundException("게시물을 찾을 수 없습니다.");
		}
		return _approval.get();
	}
	
	public Page<Approval> findByLike(Users users, String search , Departments department , String status , int page , int quantity) {
		String _search = "%"+search+"%";
		Pageable pageable = getPageable(page , quantity);
		if(status.equals("queue")) {
			return this.approvalRepository.findByUserAndStatusAndTitleLike(users, status, _search, pageable);
		}
		return this.approvalRepository.findByDepartmentAndStatusAndTitleLike(department, status, _search, pageable);
	}
	public void delete(Approval approval) {
		this.approvalRepository.delete(approval);
		
	}

	public void deleteApproval(Users users) {
		List<Approval> list = this.approvalRepository.findByUser(users);
		if(!list.isEmpty() || list.isEmpty()) {
			for(Approval approval : list) {
				approval.setUser(null);
				this.approvalRepository.save(approval);
			}
		}
	}
	public Page<Approval> myApprovalList(Users user , String status , int page , int quantity) {
		Pageable pageable = getPageable(page, quantity);
		return this.approvalRepository.findByUserAndStatus(user, status, pageable);
	}
	public Page<Approval> findByLike(Users users , String search , String status , int page , int quantity) {
		String _search = "%"+search+"%";
		Pageable pageable = getPageable(page, quantity);
		return this.approvalRepository.findByUserAndStatusAndTitleLike(users, status, _search, pageable);
	}
	
	public List<Approval> findByUser(Users users, String status) {
		return this.approvalRepository.findByUserAndStatus(users, status);

	}
}
