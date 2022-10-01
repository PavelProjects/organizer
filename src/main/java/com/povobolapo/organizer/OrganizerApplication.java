package com.povobolapo.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.povobolapo.organizer")
public class OrganizerApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OrganizerApplication.class, args);
	}

}
