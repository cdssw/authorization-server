package com.moim.authorization.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moim.authorization.entity.User;
import com.moim.authorization.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * UserDetailsServiceImpl.java
 * 
 * @author cdssw
 * @since 2020. 5. 15.
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * 2020. 5. 15.    cdssw            최초 생성
 * </pre>
 */
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		
		// 사용자가 존재하는지 확인
		if(user == null) {
			throw new UsernameNotFoundException("User not found : " + username);
		}
		
		// 로그인 한 사용자의 정보를 리턴 (이 정보로 실제 Spring Security가 검증) 
		return makeLoginUser(user);
	}
	
	private UserDetailsImpl makeLoginUser(User user) {
		UserDetailsImpl loginUser = new UserDetailsImpl();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		switch(user.getUserType()) {
		case 0:
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		case 1:
			authorities.add(new SimpleGrantedAuthority("USER"));
		}
		
		loginUser.setUsername(user.getUsername());
		loginUser.setPassword(user.getPassword());
		loginUser.setAuthorities(authorities);
		
		return loginUser;
		
	}

}
