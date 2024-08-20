package com.groupware.note.user;

import org.apache.poi.util.SystemOutLogger;
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
            System.out.println("senderEmail: " + senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            System.out.println("mail: " + mail);
            message.setSubject("이메일 인증 번호");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h4>" + "이용 중이신 애플리케이션으로 돌아가 인증을 완료해 주십시오." + "</h4>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
            System.out.println("인증 메일 문자 입력 완료");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }
    
    public int sendMail(String mail) {
    	System.out.println("service=====================");
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);
        System.out.println("인증 문자 전송");
        return number;
    }

}
