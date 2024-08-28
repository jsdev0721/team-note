package com.groupware.note.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.note.user.Users;

public interface MessageRepository extends JpaRepository<Messages, Long> {
	List<Messages> findBySender(Users users);

}
