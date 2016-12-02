/**
 * 
 */
package org.edtoktay.spring_integration.file_ops;

import java.io.File;

import org.edtoktay.spring_integration.components.EmailSender;
import org.edtoktay.spring_integration.props.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author deniz.toktay
 *
 */
@Component
@ConditionalOnExpression("${file.read.active:true}")
public class ProcessFile {
	@Autowired
	FileProperties fileProperties;
	@Autowired EmailSender emailSender;

	public void isValidJSON(String json) throws Exception {
		JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			System.out.println(jsonParser.nextToken());
		}
	}

	public void process(Message<String> message) throws Exception {
		File file = message.getHeaders().get(FileHeaders.ORIGINAL_FILE, File.class);
		String fileName = (String) message.getHeaders().get("file_name");
		String content = message.getPayload();
		System.out.println(String.format("%s receive. Content: %s", fileName, content));
		try {
			isValidJSON(content);
			file.renameTo(new File(fileProperties.getSuccessPath(), file.getName()));
		} catch (Exception e) {
			file.renameTo(new File(fileProperties.getFailPath(), file.getName()));
		}
		emailSender.sendEmail();
	}
}
