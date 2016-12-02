/**
 * 
 */
package org.edtoktay.spring_integration.components;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.edtoktay.spring_integration.props.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author deniz.toktay
 *
 */
@Component
@ConditionalOnExpression("${email.active:true}")
public class EmailSender {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private EmailProperties emailProperties;

	public void sendEmail() {
		send("this is subject", "<p>this is body</p>");
	}

	private void send(String subject, String body) {

		MimeMessage email = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper emailHelper = new MimeMessageHelper(email, true);
			emailHelper.setTo(emailProperties.getTo());
			if (StringUtils.isNotBlank(emailProperties.getCc()))
				emailHelper.setCc(emailProperties.getCc());
			if (StringUtils.isNotBlank(emailProperties.getBcc()))
				emailHelper.setBcc(emailProperties.getBcc());
			emailHelper.setFrom(emailProperties.getFrom());
			emailHelper.setSubject(subject);
			emailHelper.setText(body);
			emailProperties.javaMailService().send(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
