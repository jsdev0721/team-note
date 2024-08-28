package com.groupware.note.message;






import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groupware.note.user.Users;

public interface ChatRoomRepository extends JpaRepository<ChatRooms, Long> {
	Optional<ChatRooms> findByUser1AndUser2(Users user1, Users user2);
	
	@Query("select cr from ChatRooms cr where cr.user1 = :user or cr.user2 = :user")
	List<ChatRooms> allRoomList(@Param("user") Users user);
}
