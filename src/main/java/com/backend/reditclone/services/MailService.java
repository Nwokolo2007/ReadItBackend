package com.backend.reditclone.services;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.reditclone.exceptions.SpringRedditException;
import com.backend.reditclone.models.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender mailSender;
	   private final MailContentBuilder mailContentBuilder;
	
	@Async
	void sendMail(NotificationEmail notificationEmail) throws SpringRedditException
	{
		 MimeMessagePreparator messagePreparator = mimeMessage -> {
	            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	            messageHelper.setFrom("springreddit@email.com");
	            messageHelper.setTo(notificationEmail.getRecipient());
	            messageHelper.setSubject(notificationEmail.getSubject());
	            messageHelper.setText(notificationEmail.getBody());
	        };
					try 
					{
						mailSender.send(messagePreparator);
						log.info("Activation Email sent!!");
					}
					catch(MailException e)
					{
						log.error("Exception occured when sending email",e);
						throw new SpringRedditException("Exception occured when sending mail to " + notificationEmail.getRecipient(), e);
					}
				
	}

}
