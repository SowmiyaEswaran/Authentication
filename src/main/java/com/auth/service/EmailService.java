package com.auth.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.auth.data.entity.UserDetails;
import com.auth.exception.MyTuitionCenterAppException.EmailFailedException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;
	
	@Value("${mytuitioncenter.email.subject.resetpassword}")
	private String resetPasswordSubject;
	
	@Value("${mytuitioncenter.email.subject.verificationemail}")
	private String verificationEmailSubject;

	//to be removed after qa
	@Value("${mytuitioncenter.email.recipients}")
    private String recipientString;
	
	@Autowired
	private Configuration config;
	
	//to be removed after qa
	private List<String> getRecipients() {
        return recipientString != null ? Arrays.asList(recipientString.split(",")) : null;
    }
	
	
		public void resetPasswordTriggerMail(String emailId, String url,UserDetails userdetails) {
		Map<String, Object> params = new HashMap<>();
        params.put("password_reset_link", url);
        params.put("user_name", userdetails.getUserName());

		TriggerMail(emailId,params,"resetPasswordMail.ftl",resetPasswordSubject);
	}
	
	public void validateEmailTriggerMail(String emailId, String url, UserDetails userdetails) {
		Map<String, Object> params = new HashMap<>();
        params.put("email_verification_link", url);
        params.put("user_name", userdetails.getUserName());
		TriggerMail(emailId,params,"emailVerification.ftl", verificationEmailSubject);
	}

	public void TriggerMail(String emailId,Map<String, Object> params, String templateName, String subject) {

		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			
			Template t = config.getTemplate(templateName);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, params);

			List<String> recipients = getRecipients();
            if (recipients == null || recipients.isEmpty()) {
                helper.setTo(emailId);
            } else {
            	//to be removed after qa
                helper.setTo(recipients.toArray(new String[0]));
            }
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setFrom(sender);
            javaMailSender.send(message);


		} catch (MessagingException | IOException | TemplateException e) {
			throw new EmailFailedException();
		}

	}
}