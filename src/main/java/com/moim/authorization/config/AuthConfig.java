package com.moim.authorization.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
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

/* oauth_client_details는 client_id를 관리하는 테이블
 * Spring Security OAuth2는 인증까지 총 2차 인증을 진행한다.
 * 1차는 oauth_client_details 테이블에 등록된 client(앱/사이트)인지 확인. 여기서는 client_id, client_secret을 가지고 체크
 * 2차는 직접 관리하는 User테이블을 통해 가입된 client(사람)인지 확인. 여기서는 username, password, +(alpha)를 가지고 체크
 */
@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

	/* oauth_client_details 테이블에 접근하기 위한 서비스
	 * 아래쪽 @Bean으로 설정된 ClientDetailsService 인터페이스를 구현한 JdbcClientDetailsService가 DI된다.
	 * 이때 oauth_client_details 테이블은 schema.sql에 설정된 테이블/데이터를 활용한다.
	 * DB 설정은 하지 않았으므로 H2 메모리 DB를 사용
	 * JDBC URL: jdbc:h2:mem:testdb 로 접속
	 */
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	/* UserDetailsService 인터페이스를 구현한 UserDetailsServiceImpl가 DI된다.
	 * refresh_token으로 acccess_token을 재발급시 security-context에 저장된 principal로 User 테이블에서 계정정보를 조회
	 */
	@Autowired
	private UserDetailsService userDetailsService;
	
	/* 실제 인증처리를 하는 객체
	 * WebSecurityConfigurerAdapter를 확장한 WebSecurityConfig에서 @Bean @Override 설정된다.
	 * 
	 * 인증순서
	 * AuthenticationManager 인터페이스는 단 하나의 authenticate 메서드를 가지고 있다.
	 * AuthenticationManager 인터페이스의 구현체 ProviderManager가 실제 authenticate를 호출한다.
	 * 
	 * ProviderManager는 AuthenticationProvider 인터페이스를 has-a 관계로 가지고 있다.
	 * AuthenticationProvider 인터페이스는 authenticate 메서드를 가지고 있다.
	 * 
	 * ProviderManager에서 authenticate 메서드를 호출하면 AuthenticationProvider의 authenticate 메서드를 호출하게 된다.
	 * AuthenticationProvider 의 구현체는 3가지(Dao, Ldap, OpenId)가 있는데 여기에선 DaoAuthenticationProvider를 사용한다.
	 * 
	 * DaoAuthenticationProvider는 AbstractUserDetailsAuthenticationProvider 클래스를 상속받은 구현체이다.
	 * 최종 authenticate 메서드 호출 순서는 다음과 같다.
	 * AuthenticationManager(인터페이스) → ProviderManager → AuthenticationProvider(인터페이스) → AbstractUserDetailsAuthenticationProvider
	 * 
	 * AbstractUserDetailsAuthenticationProvider에서 authenticate 메서드가 호출되면 아래의 순서로 인증 절차를 시작한다.
	 * retrieveUser
	 * - userDetailsService의 loadUserByUsername을 통해 계정 정보 조회
	 * preAuthenticationChecks.check
	 * - 계정 잠김여부 확인 (isAccountNonLocked)
	 * - 계정 만료여부 확인 (isAccountExpired)
	 * - 계정 사용가능여부 확인 (isEnabled)
	 * additionalAuthenticationChecks (username, password 등 확인)
	 * postAuthenticationChecks.check
	 * - 계정의 패스워드 만료여부 확인 (isCredentialsNonExpired)
	 * 
	 * 성공시 사용자의 principal, credential 정보를 security-context에 저장, UsernamePasswordAuthenticationToken 생성후 인증성공 처리후 리턴
	 * 실패시 각각에 맞는 Exception throw
	 * 
	 * Extra 검증. DaoAuthenticationProvider를 확장한 클래스를 생성후 authenticate 호출 이후 조건에 따라 추가 검증로직을 넣는다.
	 * (CustomAuthenticationProvider.java 참고)
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// application.yml에 정의된 security properties 정보 load
	@Autowired
	private ResourceServerProperties resourceServerProperties;
	
	/* 인증서버 자체의 보안정보를 설정하는 부분
	 * 발급받은 access_token이 유효한지 확인할수 있는 기능을 추가할수 있다.
	 * Resource Server에서 받은 access_token이 유효한지 여부를 Authorization Server로 다시 보내서 확인해야 한다.
	 * jwt 방식은 token에 만료정보가 있기 때문에 설정할 필요 없다.
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		/* /oauth/check_token 을 활성화 */ 
//		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	
	// 클라이언트(App/Site) 인증처리를 설정하는 부분
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 클라이언트에 대한 관리를 oauth_client_details 테이블로 관리
		clients.withClientDetails(clientDetailsService); // clientDetailsService는 아래 @Bean @Primary로 정의된 JdbcClientDetailsService
	}
	
	// Endpoint에 대한 정보를 설정하는 부분
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.userDetailsService(userDetailsService) // refresh_token으로 access_token 재발급시 User 테이블 접근용도
			.accessTokenConverter(jwtAccessTokenConverter()) // 인증처리를 jwt 방식으로 설정
			.authenticationManager(authenticationManager);
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
	@Primary // 기본 ClientDetailsService 보다 우선순위로 autowired 되도록 설정
	public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
		return new JdbcClientDetailsService(dataSource);
	}
}
