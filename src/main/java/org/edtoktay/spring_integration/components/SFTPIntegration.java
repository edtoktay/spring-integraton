/**
 * 
 */
package org.edtoktay.spring_integration.components;

import java.io.File;

import org.edtoktay.spring_integration.props.FTPProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * @author deniz.toktay
 *
 */
@Configuration
@ConditionalOnExpression("${sftp.active:true}")
public class SFTPIntegration {
	@Autowired
	FTPProperties ftpProperties;

	@Bean
	public SessionFactory<LsEntry> sftpSessionFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true) {
			{
				setHost(ftpProperties.getUrl());
				setPort(Integer.valueOf(ftpProperties.getPort()));
				setUser(ftpProperties.getUsername());
				setPassword(ftpProperties.getPassword());
				setAllowUnknownKeys(true);
			}
		};
		return new CachingSessionFactory<LsEntry>(factory);
	}

	@Bean
	public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
		SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory()) {
			{
				setDeleteRemoteFiles(true);
				setRemoteDirectory(ftpProperties.getRemotedirectory());
				setFilter(new SftpSimplePatternFileListFilter("*.txt"));
			}
		};
		return fileSynchronizer;
	}

	@Bean
	@InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(fixedDelay = "${sftp.delay}"))
	public MessageSource<File> sftpMessageSource() {
		SftpInboundFileSynchronizingMessageSource messageSource = new SftpInboundFileSynchronizingMessageSource(
				sftpInboundFileSynchronizer()) {
			{
				setLocalDirectory(new File(ftpProperties.getLocaldirectory()));
				setAutoCreateLocalDirectory(true);
				setLocalFilter(new AcceptOnceFileListFilter<File>());
			}
		};
		return messageSource;
	}

	@Bean
	@ServiceActivator(inputChannel = "sftpChannel")
	public MessageHandler messageHandler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> arg0) throws MessagingException {
				System.out.println(arg0.getPayload());
			}
		};
	}
}
