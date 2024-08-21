package com.groupware.note.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
	private final MessageRepository mRepo;
	private final ChatRoomRepository crRepo;
	private final UserService uService;
	
	//대화방 생성
	private ChatRooms createChatRoom(Users user1, Users user2) {
		ChatRooms chatRoom = new ChatRooms();
		chatRoom.setUser1(user1);
		chatRoom.setUser2(user2);
		return this.crRepo.save(chatRoom);
	}
	//대화내용 저장
	public Messages saveMessage(Users sender, Users receiver, String content) {
		Messages message = new Messages();
		message.setContent(content);
		message.setSendTime(LocalDateTime.now());
		Optional<ChatRooms> _chatRoom1 = this.crRepo.findByUser1AndUser2(sender, receiver);
		
		if(_chatRoom1.isPresent()) {
			ChatRooms chatRoom = _chatRoom1.get();
			message.setSender(sender);
			message.setChatRoom(chatRoom);
			return this.mRepo.save(message);
		} else {
			Optional<ChatRooms> _chatRoom2 = this.crRepo.findByUser1AndUser2(receiver, sender);
			if(_chatRoom2.isPresent()) {
				ChatRooms chatRoom = _chatRoom2.get();
				message.setSender(sender);
				message.setChatRoom(chatRoom);
				return this.mRepo.save(message);
			} else {
				ChatRooms chatRoom = createChatRoom(sender, receiver);
				message.setSender(sender);
				message.setChatRoom(chatRoom);
				return this.mRepo.save(message);
			} //if : _chatRoom2.isPresent
		} //if : _chatRoom1.isPresent
	}// method : saveMessage
	
	//기존 대화내용 불러오기
	public List<Messages> getMessagesBetweenUsers(String username1, String username2){
		Users user1 = this.uService.getUser(username1);
		Users user2 = this.uService.getUser(username2);
		
		Optional<ChatRooms> cr1 = this.crRepo.findByUser1AndUser2(user1, user2);
		
		if(cr1.isPresent()) {
			System.out.println("=============1===================================");
			return cr1.get().getMessages();
		} else {
			Optional<ChatRooms> cr2 = this.crRepo.findByUser1AndUser2(user2, user1);
			if(cr2.isPresent()) {
				System.out.println("=============233===================================");
				return cr2.get().getMessages();
			} else {
				System.out.println("=============222222222222222===================");
				return new ArrayList<>();
			}//if : cr2.isPresent
		} //if : cr1.isPresent
	}
}
