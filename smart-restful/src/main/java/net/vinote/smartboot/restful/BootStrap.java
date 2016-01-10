package net.vinote.smartboot.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ComponentScan
@ImportResource(
	locations = { "classpath*:service-config.xml","classpath*:mvc-config.xml" })
public class BootStrap {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(BootStrap.class, args);
	}
}
