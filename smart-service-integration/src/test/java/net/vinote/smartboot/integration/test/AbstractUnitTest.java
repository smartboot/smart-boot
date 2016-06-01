package net.vinote.smartboot.integration.test;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(false)
@SpringApplicationConfiguration(
	locations = { "classpath*:service-integration.xml" }, classes = AbstractUnitTest.class)
@IntegrationTest
@SpringBootApplication(
	scanBasePackages = { "com.kiduke.h5" })
public abstract class AbstractUnitTest {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AbstractUnitTest.class, args);
	}
}