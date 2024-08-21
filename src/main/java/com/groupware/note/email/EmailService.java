package com.groupware.note.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender javaMailSender;
	private static final String senderEmail = "oneuldo07@gmail.com";
	private static int number;
	
	// 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;
    }
    
    public MimeMessage CreateMail(String mail) {
        createNumber();
        System.out.println("number: " + number);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("[NoTE] 이메일 인증코드");
            String body = "";
            body += "<p>" + "안녕하세요, NoTE입니다." + "</p>";
            body += "<p>" + "이메일 주소 인증을 위해 요청하신 6자리 코드는 다음과 같습니다." + "</p>";
            body += "<p>" + "인증 코드 : " + "<strong>" + number + "</strong>" + "</p>";
            body += "<p>" + "이용 중이신 애플리케이션으로 돌아가 인증을 완료해 주십시오." + "</p>";
            body += "<p>" + "감사합니다." + "</p>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }
    
    public int sendMail(String mail) {
    	System.out.println("service=====================");
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);
        return number;
    }

}
