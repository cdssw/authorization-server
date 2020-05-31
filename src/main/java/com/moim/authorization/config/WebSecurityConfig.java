package com.moim.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfig.java
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
@EnableWebSecurity // 직접 관리하는 User 테이블을 통해 client(사람)인지 확인
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// UserDetailsService 인터페이스를 구현한 UserDetailsServiceImpl이 DI된다.
	@Autowired
	private UserDetailsService userDetailsService;
	
	/* client(사용자) 인증처리 방식 등록
	 * DaoAuthenticationProvider는 UserDetailsService로 사용자 정보를 DB에서 조회 UserDetails 객체를 리턴
	 * 요청된 정보와 비교 후 인증처리
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	// 인증처리를 DB를 통해 하도록 AuthenticationProvider(DaoAuthenticationProvider 확장)로 설정
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService); // 사용자 정의 UserDetailsService 주입
		authenticationProvider.setPasswordEncoder(passwordEncoder()); // password encoder 주입
		return authenticationProvider;
	}
	
	// applicationManager 빈등록 - AuthConfig에서 Autowired 됨
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}	
}
