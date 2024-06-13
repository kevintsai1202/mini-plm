package com.miniplm.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("${mail-from-user}")
	private String from;

    public void sendEmail(String to, String subjectTemplate, String bodyTemplate, Map<String, String> variables) throws MessagingException {
    	
    	String body = generateEmailTemplate(bodyTemplate, variables);
        String subject = generateEmailTemplate(subjectTemplate, variables);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom("NPI System<"+from+">");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        
        mailSender.send(message);
    }

    private String generateEmailTemplate(String template, Map<String, String> variables) {
        String body = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
        	String key = entry.getKey();
            String value = entry.getValue();
            body = body.replace("$$" + key + "$$", value);
        }
        return body;
    }
}
