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
@ConfigurationProperties("sftp")
@ConditionalOnExpression("${sftp.active:true}")
public class FTPProperties {
	@Value("${sftp.host}")
	private String url;
	@Value("${sftp.port}")
	private String port;
	@Value("${sftp.username}")
	private String username;
	@Value("${sftp.password}")
	private String password;
	@Value("${sftp.directory.remote}")
	private String remotedirectory;
	@Value("${sftp.directory.local}")
	private String localdirectory;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getRemotedirectory() {
		return remotedirectory;
	}
	public void setRemotedirectory(String remotedirectory) {
		this.remotedirectory = remotedirectory;
	}
	public String getLocaldirectory() {
		return localdirectory;
	}
	public void setLocaldirectory(String localdirectory) {
		this.localdirectory = localdirectory;
	}
}
