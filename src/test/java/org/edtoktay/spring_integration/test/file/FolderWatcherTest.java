/**
 * 
 */
package org.edtoktay.spring_integration.test.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.edtoktay.spring_integration.Application;
import org.edtoktay.spring_integration.props.FileProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * @author deniz.toktay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class FolderWatcherTest {
	@Autowired FileProperties fileProperties;
	private GreenMail smtpGreenMail;
	private CountDownLatch latch = new CountDownLatch(1);
	private final String virtualDir = new FileSystemResource("").getFile().getAbsolutePath();
	private String TEST_FILE_PATH = "\\src\\test\\resources\\test_files\\";
	private String TEST_FOLDER_PATH = "\\src\\test\\resources\\folder_test\\";
	
	private void removeTestFiles() throws IOException{
		FileUtils.cleanDirectory(new File(fileProperties.getFailPath()));
		FileUtils.cleanDirectory(new File(fileProperties.getSuccessPath()));
	}
	@Before
	public void setUp() throws Exception {
		removeTestFiles();
		smtpGreenMail = new GreenMail(new ServerSetup(25, "127.0.0.1", "smtp"));
		smtpGreenMail.start();
		smtpGreenMail.setUser("test@localhost", "test1234", "test1234");
		fileProperties.setLocalpath(virtualDir + TEST_FOLDER_PATH);
		System.out.println(fileProperties.getLocalpath());
		File file1 = new File(virtualDir + TEST_FILE_PATH + "fail_file.txt");
		File file2 = new File(virtualDir + TEST_FILE_PATH + "success_file.txt");
		File directory = new File(virtualDir + TEST_FOLDER_PATH);
		FileUtils.copyFileToDirectory(file1, directory);
		FileUtils.copyFileToDirectory(file2, directory);
	}

	@After
	public void tearDown() throws Exception {
		smtpGreenMail.stop();
		removeTestFiles();
	}

	@Test
	public void test() throws InterruptedException {
		latch.await(20, TimeUnit.SECONDS);
		assertTrue((new File(fileProperties.getFailPath() + "fail_file.txt")).exists());
		assertTrue((new File(fileProperties.getSuccessPath() + "success_file.txt")).exists());
	}

}
