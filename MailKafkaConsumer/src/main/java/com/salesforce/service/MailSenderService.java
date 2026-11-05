package com.salesforce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MailSenderService {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private  ResourceLoader resourceLoader;



	public void sendHtmlEmail(String to, String subject) throws MessagingException, IOException {
		String htmlContent = loadHtmlContent();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
		helper.setFrom("aymansce@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);
		mailSender.send(message);
	}

	private String loadHtmlContent() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:email.html");
		byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
