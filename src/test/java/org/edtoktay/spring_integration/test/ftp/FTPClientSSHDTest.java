/**
 * 
 */
package org.edtoktay.spring_integration.test.ftp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.edtoktay.spring_integration.Application;
import org.edtoktay.spring_integration.props.FTPProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author deniz.toktay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class FTPClientSSHDTest {
	@Autowired FTPProperties ftpProperties;
	private SshServer sshd;
	private CountDownLatch latch = new CountDownLatch(1);
	private final String virtualDir = new FileSystemResource("").getFile().getAbsolutePath();
	private String TEST_FILE_PATH = "\\src\\test\\resources\\test_files\\";
	private String TEST_SFTP_PATH = "\\src\\test\\resources\\ftp_test\\";
	
	private void copyFileToDirectory() throws IOException{
		File file1 = new File(virtualDir + TEST_FILE_PATH + "test.txt");
		File file2 = new File(virtualDir + TEST_SFTP_PATH);
		FileUtils.copyFileToDirectory(file1, file2);
	}
	
	@Before
	public void setUp() throws Exception {
		copyFileToDirectory();
		sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9999);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			
			@Override
			public boolean authenticate(String arg0, String arg1, ServerSession arg2) {
				return "eren".equalsIgnoreCase(arg0) && "deniz".equalsIgnoreCase(arg1);
			}
		});
		CommandFactory commandFactory = new CommandFactory() {
			
			@Override
			public Command createCommand(String arg0) {
				return null;
			}
		};
		sshd.setCommandFactory(new ScpCommandFactory(commandFactory));
		List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
		namedFactoryList.add(new SftpSubsystem.Factory());
		sshd.setSubsystemFactories(namedFactoryList);
		sshd.setFileSystemFactory(new VirtualFileSystemFactory(virtualDir));
		sshd.start();
	}


	@After
	public void tearDown() throws Exception {
		sshd.stop();
	}
	
	@Test
	public void test() throws InterruptedException, IOException {
		String testFileDir = ftpProperties.getLocaldirectory() + "test.txt";
		File file = new File(testFileDir);
		FileUtils.deleteQuietly(file);
		assertTrue(!file.exists());
		latch.await(10, TimeUnit.SECONDS);
		assertTrue(file.exists());
		FileUtils.forceDeleteOnExit(file);
	}

}
