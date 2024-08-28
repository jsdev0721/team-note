package com.groupware.note.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;
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
	
	//대화 읽음 표시
	public void readMessage(Messages message) {
		this.mRepo.save(message);
	}
	
	//대화내용 저장
	public Messages saveMessage(Users sender, Users receiver, String content) {
		Messages message = new Messages();
		message.setContent(content);
		message.setSendTime(LocalDateTime.now());
		message.setSeenByR(0);
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
	
	//전체 안읽은 대화 목록
	public int getAllUnreadMessage(Users user) {
		
		List<ChatRooms> crList = new ArrayList<>();
		crList = this.crRepo.allRoomList(user);
		int noRM = 0;
		for(ChatRooms cr : crList) {
			List<Messages> _mlist = this.mRepo.findByNotSender(cr, user);
			noRM = noRM + _mlist.size();
			System.out.println("=====================================================");
			System.out.println(noRM);
		}
		return noRM;
	}
	
	
	//안읽은 대화 목록
	public Integer getUnreadMessage(Users user, ChatRooms chatRoom) {
		List<Messages> _list = null;
		return null;
	}
	
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
	public void deleteMessage(Users user) {
		List<Messages> list =this.mRepo.findBySender(user);
		if(!list.isEmpty() || list.isEmpty()) {
			for(Messages messages : list) {
				messages.setSender(null);
				this.mRepo.delete(messages);
				}
			}else {throw new DataNotFoundException("데이터가 없습니다");}	
	}
}
