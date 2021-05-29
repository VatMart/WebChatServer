package ru.vatmart.webchatserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.services.UserService;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WebchatServerApplication {

//	@Autowired
//	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(WebchatServerApplication.class, args);
	}

//	@PostConstruct
//	public void init() {
//		User user1 = new User();
//		user1.setLogin("LogTest1");
//		user1.setPassword("PasTest1");
//		user1.setNickname("NickTest1");
//		user1.setRole("regular");
//		User user2 = new User();
//		user2.setLogin("LogTest2");
//		user2.setPassword("PasTest2");
//		user2.setNickname("NickTest2");
//		user2.setRole("regular");
//		userService.createUser(user1);
//		userService.createUser(user2);
//		userService.printUsers();
//	}

}
