package com.groupware.note.message;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupware.note.message.MessageService.URMessageData;
import com.groupware.note.message.MessageService.UserListForDep;
import com.groupware.note.user.UserDetails;
import com.groupware.note.user.UserDetailsService;
import com.groupware.note.user.UserService;
import com.groupware.note.user.Users;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessageController {
	private final MessageService mService;
	private final UserService uService;
	
	//대화방에 사용자 리스트 출력
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/message/list")
	public String get(Model model, Principal principal) {
		List<UserListForDep> depList = this.mService.messageListForDep(model, principal);
		model.addAttribute("depList", depList);
		model.addAttribute("me", this.uService.getUser(principal.getName()));
		
		return "message";
	}
	
	//대화내용을 saveMessage로 보내 저장한다
	@MessageMapping("/message.sendMessage")
	@SendTo("/topic/messages")
	public Messages sendMessage(Messages message, Principal principal) {
		if(!message.getContent().isBlank()) {
			Users sender = this.uService.getUser(principal.getName());
			Users reseiver = this.uService.getUser(message.getChatRoom().getUser2().getUsername());
			String content = message.getContent();
			return this.mService.saveMessage(sender, reseiver, content);
		}
		return null;
	}
	
	//기존 대화방 대화내용 불러오기
	@GetMapping("/message/messages/{username2}")
	@ResponseBody
	public List<Messages> getMessage(@PathVariable("username2") String username2, Principal principal){
		List<Messages> list = this.mService.getMessagesBetweenUsers(principal.getName(), username2);
		Users _sender = this.uService.getUser(principal.getName());
		for(Messages m : list) {
			if(!m.getSender().equals(_sender)) {
				if(m.getSeenByR()==0) {
					m.setSeenByR(1);
					this.mService.readMessage(m);
				}
			}
		}
		return list;
	}
	
	//안읽은 메세지 숫자 체크
	@GetMapping("/message/unread")
	@ResponseBody
	public int getNoRM(Model model, Principal principal) {
		Users user = this.uService.getUser(principal.getName());
		int noReadMessages = this.mService.getAllUnreadMessage(user);
		return noReadMessages;
	}
	
	//메세지 보낸 사용자 이름 출력
	@GetMapping("/message/sendUser")
	@ResponseBody
	public String getSendUser(Principal principal) {
		Users user = this.uService.getUser(principal.getName());
		List<URMessageData> list = this.mService.getUnreadUser(user);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
}
