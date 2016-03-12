package net.vinote.smartboot.dal.test;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(false)
@Transactional(
	transactionManager = "transactionManager")
@SpringApplicationConfiguration(
	classes = AbstractUnitTest.class)
@IntegrationTest
@SpringBootApplication(
	scanBasePackages = { "net.vinote.smartboot" })
public abstract class AbstractUnitTest {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AbstractUnitTest.class, args);
	}
}