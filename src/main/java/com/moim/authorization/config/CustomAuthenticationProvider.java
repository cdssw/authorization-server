package com.moim.authorization.config;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moim.authorization.user.LoginDto;

/**
 * CustomAuthenticationProvider.java
 * 
 * @author cdssw
 * @since 2020. 5. 29.
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * 2020. 5. 29.   cdssw            최초 생성
 * </pre>
 */
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		Authentication auth = super.authenticate(authentication);
		
		// 인증이 성공하면 추가 인증을 위해 extra field를 검사한다.
		if(auth.isAuthenticated()) {
			ModelMapper modelMapper = new ModelMapper();
			LoginDto dto = modelMapper.map(authentication.getDetails(), LoginDto.class);
			if("password".equals(dto.getGrant_type())) {
				if(dto.getCommon() == null) {
					throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials"));
				}
				
				PasswordEncoder passwordEncoder = getPasswordEncoder();
				if(!passwordEncoder.matches("common", passwordEncoder.encode(dto.getCommon()))) {
					throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials"));
				}
			}
		}
		
		return auth;
	}
}
