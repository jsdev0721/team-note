package com.groupware.note.message;



import java.util.List;

import org.springframework.stereotype.Service;

import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class chatRoomService {
	 private final ChatRoomRepository chatRoomRepository;
	 
	 public void deleteChatRoom(Users user1) {
		List<ChatRooms> chatRoom=this.chatRoomRepository.findByUser1OrUser2(user1,user1);
		if(!chatRoom.isEmpty()) {
			for(ChatRooms chat : chatRoom) {
				this.chatRoomRepository.delete(chat); 
			}
		}
	}
}
