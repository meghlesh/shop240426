package com.cws.shop.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cws.shop.model.Role;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public DataInitializer(UserRepository userRepository,PasswordEncoder passwordEncoder){
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
	} 
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		if(!userRepository.existsByEmail("superadmin@shop.com")){

			User superAdmin=new User();

			superAdmin.setName("Super Admin");
			superAdmin.setEmail("superadmin@shop.com");
			superAdmin.setMobileNumber("9999999999");
			superAdmin.setPassword(passwordEncoder.encode("SuperAdmin@123"));
			superAdmin.setRole(Role.SUPER_ADMIN);
			superAdmin.setActive(true);

			userRepository.save(superAdmin);
		}
 
	}
}
