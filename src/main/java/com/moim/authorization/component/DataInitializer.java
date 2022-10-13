package com.moim.authorization.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.moim.authorization.entity.Policy;
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
					.userNm("Andrew")
					.userNickNm("Blue")
					.phone("010-1111-1111")
					.mainTalent("프로그램 개발")
					.talent("자바,웹 개발,MSA,파이썬")
					.interest("음악,영상")
					.policy(Policy.builder().serviceYn(true).privateYn(true).profileYn(true).build())
					.build();
			userRepository.save(user);
		}
		
		if(userRepository.findByUsername("loh002@naver.com") == null) {
			User user = User.builder()
					.username("loh002@naver.com")
					.password(passwordEncoder.encode("1234"))
					.userType(0)
					.userNm("Monica")
					.userNickNm("HK")
					.phone("010-2222-2222")
					.mainTalent("부동산")
					.talent("기타")
					.interest("주택")
					.policy(Policy.builder().serviceYn(true).privateYn(true).profileYn(true).build())
					.build();
			userRepository.save(user);
		}
		
		if(userRepository.findByUsername("michael@naver.com") == null) {
			User user = User.builder()
					.username("michael@naver.com")
					.password(passwordEncoder.encode("1234"))
					.userType(0)
					.userNm("Michael")
					.userNickNm("Tester")
					.phone("010-3333-3333")
					.mainTalent("샘플")
					.talent("그림,편집,컴퓨터")
					.interest("유튜브,산")
					.policy(Policy.builder().serviceYn(true).privateYn(true).profileYn(true).build())
					.build();
			userRepository.save(user);
		}
		
		if(userRepository.findByUsername("nss@nss.com") == null) {
			User user = User.builder()
					.username("nss@nss.com")
					.password(passwordEncoder.encode("1189"))
					.userType(0)
					.userNm("Newsongsearch")
					.userNickNm("Nss")
					.phone("010-0000-0000")
					.mainTalent("Nss")
					.talent("Elastic")
					.interest("None")
					.policy(Policy.builder().serviceYn(true).privateYn(true).profileYn(true).build())
					.build();
			userRepository.save(user);
		}
	}

}
