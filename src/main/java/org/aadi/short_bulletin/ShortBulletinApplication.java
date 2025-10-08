package org.aadi.short_bulletin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.aadi.short_bulletin.service.UserService;

@SpringBootApplication
public class ShortBulletinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortBulletinApplication.class, args);
	}
}
