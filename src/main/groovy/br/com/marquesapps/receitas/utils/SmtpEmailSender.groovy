package br.com.marquesapps.receitas.utils

import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SmtpEmailSender{

	@Autowired
	private JavaMailSender javaMailSender
	
	def send(String to, String subject, String body) throws MessagingException{
		
		MimeMessage message = javaMailSender.createMimeMessage()
		MimeMessageHelper helper
		
		helper = new MimeMessageHelper(message, true) //true = multipart message
		helper.setFrom("liunit@gmail.com")
		helper.setSubject(subject)
		helper.setTo(to)
		helper.setText(body, true)
		
		return javaMailSender.send(message)
	}
}
