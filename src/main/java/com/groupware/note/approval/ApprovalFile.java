package com.groupware.note.approval;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ApprovalFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer approvalFileId;
	
	private String originFileName;
	
	private String storeFileName;
	
	@ManyToOne
	@JoinColumn(name = "Approval_id")
	private Approval approval;
}
