package com.example.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.repository.UserRepository;

@SpringBootApplication
public class SpringsecurityApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);
	}
	
	public void run(String ...args) {
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if(adminAccount == null) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstname("ADMIN");
			user.setSecondname("ADMIN");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));			
			userRepository.save(user);
		}
	}

}
