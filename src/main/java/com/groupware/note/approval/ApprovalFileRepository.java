package com.groupware.note.approval;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFileRepository extends JpaRepository<ApprovalFile, Integer> {
	List<ApprovalFile> findByApproval(Approval approval);
	Optional<ApprovalFile> findByApprovalAndOriginFileName(Approval approval , String originFileName);
}
