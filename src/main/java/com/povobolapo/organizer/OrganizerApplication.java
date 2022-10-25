package com.povobolapo.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.povobolapo.organizer")
public class OrganizerApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrganizerApplication.class, args);
	}

}
