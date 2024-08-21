package com.groupware.note.message;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.note.user.Users;

public interface ChatRoomRepository extends JpaRepository<ChatRooms, Long> {
	Optional<ChatRooms> findByUser1AndUser2(Users user1, Users user2);
}
