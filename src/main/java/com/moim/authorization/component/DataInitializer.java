package com.moim.authorization.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.moim.authorization.entity.User;
import com.moim.authorization.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * DataInitializer.java
 * 
 * @author cdssw
 * @since 2020. 5. 15.
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * 2020. 5. 15.   cdssw            최초 생성
 * </pre>
 */
@AllArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

	private UserRepository userRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		if(userRepository.findByUsername("cdssw@naver.com") == null) {
			User user = User.builder()
					.username("cdssw@naver.com")
					.password(passwordEncoder.encode("1234"))
					.userType(0)
					.build();
			userRepository.save(user);
		}
		
		if(userRepository.findByUsername("loh002@naver.com") == null) {
			User user = User.builder()
					.username("loh002@naver.com")
					.password(passwordEncoder.encode("1234"))
					.userType(0)
					.build();
			userRepository.save(user);
		}
		
		if(userRepository.findByUsername("michael@naver.com") == null) {
			User user = User.builder()
					.username("michael@naver.com")
					.password(passwordEncoder.encode("1234"))
					.userType(0)
					.build();
			userRepository.save(user);
		}
	}

}
