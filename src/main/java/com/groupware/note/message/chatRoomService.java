package com.groupware.note.message;



import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class chatRoomService {
	 private final ChatRoomRepository chatRoomRepository;
	 
	 public void deleteChatRoom(Users user1,Users user2) {
		 Optional<ChatRooms> chatRoom=this.chatRoomRepository.findByUser1AndUser2(user1, user2);
		 if(chatRoom.isPresent()) {
			 this.chatRoomRepository.delete(chatRoom.get());
		 }
	 }
}
