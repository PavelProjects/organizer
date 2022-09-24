package com.povobolapo.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.povobolapo.organizer")
public class OrganizerApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OrganizerApplication.class, args);
	}

}
