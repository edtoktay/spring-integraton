/**
 * 
 */
package org.edtoktay.spring_integration.test;

import org.edtoktay.spring_integration.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author deniz.toktay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
@WebAppConfiguration
public class ApplicationTest {
	@Test
	public void contextLoads(){
		
	}
}
