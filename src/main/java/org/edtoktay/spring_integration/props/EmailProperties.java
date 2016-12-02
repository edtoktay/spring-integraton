/**
 * 
 */
package org.edtoktay.spring_integration.props;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author deniz.toktay
 *
 */
@Configuration
@ConfigurationProperties("email")
@ConditionalOnExpression("${email.active:true}")
public class EmailProperties {
	@Value("${email.host}")
	private String host;
	@Value("${email.port}")
	private String port;
	@Value("${email.username}")
	private String username;
	@Value("${email.password}")
	private String password;
	@Value("${email.password}")
	private String from;
	@Value("${email.password}")
	private String to;
	@Value("${email.password}")
	private String cc;
	@Value("${email.password}")
	private String bcc;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(getHost());
		sender.setPort(Integer.valueOf(getPort()));
		sender.setUsername(getUsername());
		sender.setPassword(getPassword());
		sender.setJavaMailProperties(getMailProperties());
		return sender;
	}
	private Properties getMailProperties() {
		Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "false");
        return properties;
	}
}
