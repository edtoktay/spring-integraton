/**
 * 
 */
package org.edtoktay.spring_integration.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author deniz.toktay
 *
 */
@Configuration
@ConfigurationProperties("file")
@ConditionalOnExpression("${file.read.active:true}")
public class FileProperties {
	@Value("${file.read.directory.file}")
	private String localpath;
	@Value("${file.read.directory.fail}")
	private String failPath;
	@Value("${file.read.directory.success}")
	private String successPath;
	public String getLocalpath() {
		return localpath;
	}
	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}
	public String getFailPath() {
		return failPath;
	}
	public void setFailPath(String failPath) {
		this.failPath = failPath;
	}
	public String getSuccessPath() {
		return successPath;
	}
	public void setSuccessPath(String successPath) {
		this.successPath = successPath;
	}
}
