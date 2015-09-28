package br.com.marquesapps.receitas.utils;

import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
public class SmtpMailSender {
	
	def send(String to, String subject, String body) throws MessagingException {
		
		// of course you would use DI in any real-world cases
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Properties javaMailProperties = new Properties();		
		sender.setHost("smtp.gmail.com");
		sender.setUsername("");
	    sender.setPassword("");
		sender.setPort(465);		
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.required","true");
		sender.setJavaMailProperties(javaMailProperties);
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message , true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body , true);
		
		return sender.send(message);
	}

}
