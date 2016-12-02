/**
 * 
 */
package org.edtoktay.spring_integration.components;

import java.io.File;

import org.edtoktay.spring_integration.props.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author deniz.toktay
 *
 */
@Component
@ConditionalOnExpression("${file.read.active:true}")
public class FileReadIntegration {
	@Autowired
	FileProperties fileProperties;

	@Bean
	public MessageChannel fileChannel() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow processFlow() {
		return IntegrationFlows.from("fileChannel").transform(fileToStringTransformer())
				.handle("processFile", "process").get();
	}

	@Bean
	@Transformer(inputChannel = "fileChannel", outputChannel = "process")
	public FileToStringTransformer fileToStringTransformer() {
		return new FileToStringTransformer();
	}

	@Bean
	@InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "${file.read.delay}"))
	public MessageSource<File> fileReadingSource() {
		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setDirectory(new File(fileProperties.getLocalpath()));
		source.setFilter(new SimplePatternFileListFilter("*.txt"));
		return source;
	}
}
