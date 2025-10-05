package org.aadi.short_bulletin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.aadi.short_bulletin.service.UserService;

@SpringBootApplication
public class ShortBulletinApplication {
	private final UserService userService;

	public ShortBulletinApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShortBulletinApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void createDefaultAdmin() {
		userService.createDefaultAdmin();
	}
}