package br.com.marquesapps.receitas.utils;

import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
public class SmtpMailSender {
	
	@Value('${spring.mail.username}')
	private String username;
	@Value('${spring.mail.password}')
	private String password;
	@Value('${spring.mail.host}')
	private String host;
	@Value('${spring.mail.port}')
	private int port;
	@Value('${mail.smtp.starttls.enable}')
	private String starttlsenable;
	@Value('${mail.smtp.starttls.required}')
	private String starttlsrequired;
	@Value('${mail.smtp.auth}')
	private String smtpauth;
	
	def send(String to, String subject, String body) throws MessagingException {
		
		// of course you would use DI in any real-world cases
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Properties javaMailProperties = new Properties();		
		sender.setHost(this.host);
		sender.setUsername(this.username);
	    sender.setPassword(this.password);
		sender.setPort(this.port);		
		javaMailProperties.setProperty("mail.smtp.auth",  this.smtpauth);
		javaMailProperties.setProperty("mail.smtp.starttls.enable", this.starttlsenable);
		javaMailProperties.setProperty("mail.smtp.starttls.required", this.starttlsrequired);
		sender.setJavaMailProperties(javaMailProperties);
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message , true);
		helper.setFrom(this.username)
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body , true);
		
		return sender.send(message);
	}

}
