/**
 * 
 */
package org.edtoktay.spring_integration.test.email;

import static org.junit.Assert.*;

import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.util.MimeMessageParser;
import org.edtoktay.spring_integration.Application;
import org.edtoktay.spring_integration.components.EmailSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * @author deniz.toktay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class SmtpClientTest {
	private GreenMail smtpGreenMail;
	@Autowired EmailSender emailSender;
	@Before
	public void setUp() throws Exception {
		smtpGreenMail = new GreenMail(new ServerSetup(25, "127.0.0.1", "smtp"));
		smtpGreenMail.start();
		smtpGreenMail.setUser("test@localhost", "test1234", "test1234");
	}
	@Test
	public void test() throws Exception {
		emailSender.sendEmail();
		assertMailContains("this is body");
		assertTrue(true);
	}
	private void assertMailContains(String content) throws Exception {
		MimeMessage[] receivedMails = smtpGreenMail.getReceivedMessages();
		assertEquals(1, receivedMails.length);
		assertEquals("this is subject", receivedMails[0].getSubject());
		MimeMessageParser parser = new MimeMessageParser(receivedMails[0]).parse();
		assertEquals("<p>this is body</p>", parser.getPlainContent());
	}
	@After
	public void tearDown() throws Exception {
		smtpGreenMail.stop();
	}
}
