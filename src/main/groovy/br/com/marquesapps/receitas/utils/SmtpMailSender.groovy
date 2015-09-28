package br.com.marquesapps.receitas.utils;

import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

import br.com.marquesapps.receitas.config.Mail

@Component
public class SmtpMailSender {
	
	def send(String to, String subject, String body) throws MessagingException {
		
		// of course you would use DI in any real-world cases
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Properties javaMailProperties = new Properties();		
		sender.setHost(Mail.getHost());
		sender.setUsername(Mail.getUsername());
	    sender.setPassword(Mail.getPassword());
		sender.setPort(Mail.getPort());		
		javaMailProperties.setProperty("mail.smtp.auth",  Mail.getSmtpauth());
		javaMailProperties.setProperty("mail.smtp.starttls.enable", Mail.getStarttlsenable());
		javaMailProperties.setProperty("mail.smtp.starttls.required", Mail.getStarttlsrequired());
		sender.setJavaMailProperties(javaMailProperties);
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message , true);
		helper.setFrom(Mail.getFrom())
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body , true);
		
		return sender.send(message);
	}

}
