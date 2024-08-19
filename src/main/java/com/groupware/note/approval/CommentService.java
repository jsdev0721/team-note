package com.groupware.note.approval;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	
	public Comments save(Comments comments) {
		comments.setUpdateTime(LocalDateTime.now());
		return this.commentRepository.save(comments);
	}
}
