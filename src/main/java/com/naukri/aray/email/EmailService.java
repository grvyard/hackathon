package com.naukri.aray.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
 
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	
	public void send(Email eParams) {
		if (eParams.isHtml()) {
			try {
				sendHtmlMail(eParams);
			} catch (MessagingException e) {
				log.error("Could not send email to :  Error = {}", e.getMessage());
			}
		} else {
			sendPlainTextMail(eParams);
		}
	}
 
	private void sendHtmlMail(Email eParams) throws MessagingException {
		boolean isHtml = true;
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		helper.setReplyTo(eParams.getFrom());
		helper.setFrom(eParams.getFrom());
		helper.setSubject(eParams.getSubject());
		helper.setText(eParams.getMessage(), isHtml);
		if (eParams.getCc().size() > 0) {
			helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}
		mailSender.send(message);
	}
 
	private void sendPlainTextMail(Email eParams) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		eParams.getTo().toArray(new String[eParams.getTo().size()]);
		mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		mailMessage.setReplyTo(eParams.getFrom());
		mailMessage.setFrom(eParams.getFrom());
		mailMessage.setSubject(eParams.getSubject());
		mailMessage.setText(eParams.getMessage());
		if (eParams.getCc().size() > 0) {
			mailMessage.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}
		mailSender.send(mailMessage);
	}
}
