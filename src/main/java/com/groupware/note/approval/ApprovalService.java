package com.groupware.note.approval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
	
	public Page<Approval> ApprovalList(int page , Departments department , String status) {
		List<Sort.Order>sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("updateTime"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		List<Approval> _approvalList = this.approvalRepository.findByDepartment(department);
		List<Approval> approvalList = new ArrayList<>();
		for(Approval approval : _approvalList) {
			if(approval.getStatus().equals(status)) {
				approvalList.add(approval);
			}
		}
		return new PageImpl<>(approvalList, pageable, page);
	}
	
	public Page<Approval> ApprovalfindbyUser(int page , Users user) {
		List<Sort.Order>sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("updateTime"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.approvalRepository.findByUser(user, pageable);
	}
	
	public Approval save(Approval approval) {
		if(approval.getApprovalId()==null) {
			approval.setStatus("queue");;
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
}
