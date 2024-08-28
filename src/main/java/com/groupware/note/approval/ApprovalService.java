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
	public Page<Approval> ApprovalList(Departments department , String status , int page , int quantity) {
		Pageable pageable = getPageable(page , quantity);
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
	
	public Page<Approval> findByLike(String search , Departments department , String status , int page , int quantity) {
		String _search = "%"+search+"%";
		Pageable pageable = getPageable(page , quantity);
		return this.approvalRepository.findByDepartmentAndStatusAndTitleLike(department, status, _search, pageable);
	}
	public void deleteById(Approval approval) {
		this.approvalRepository.delete(approval);
		
	}

	public void deleteApproval(Users users) {
		List<Approval> optional = this.approvalRepository.findByUser(users);
		if(!optional.isEmpty() || optional.isEmpty()) {
			for(Approval approval : optional) {
				approval.setUser(null);
				this.approvalRepository.save(approval);
			}
		}else {throw new DataNotFoundException("데이터가 없습니다");}
	}

	
	public List<Approval> findByUser(Users users, String status) {
		return this.approvalRepository.findByUserAndStatus(users, status);

	}
}
