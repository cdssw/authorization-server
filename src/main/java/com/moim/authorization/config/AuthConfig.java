package com.moim.authorization.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * AuthConfig.java
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
@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

	// oauth_client_details 테이블에 접근하기 위한 서비스
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	// 실제 인증처리를 하는 객체
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// application.yml에 정의된 security properties 정보 load
	@Autowired
	private ResourceServerProperties resourceServerProperties;
	
	// oauth_client_details 테이블에 등록된 client를 조회 1차 검증
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService);
	}
	
	// WebSecurityConfig에 등록 provider를 가지고 2차 검증 (username, password)
	// 성공시 jwt token을 암호화해서 발급
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.accessTokenConverter(jwtAccessTokenConverter()).authenticationManager(authenticationManager);
	}
	
	@Bean	
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		
		// jwt token을 암호화 하기 위한 비밀키
		// 운영에는 KeyPair를 사용하자(인증서)
		converter.setSigningKey(resourceServerProperties.getJwt().getKeyValue());
		return converter;
	}
	
	// DB oauth_client_details 테이블을 사용하여 클라이언트 정보를 조회 및 관리
	@Bean
	@Primary
	public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
		return new JdbcClientDetailsService(dataSource);
	}
}
