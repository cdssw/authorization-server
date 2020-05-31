package com.moim.authorization;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationServerApplication.class)
@Slf4j
public class AuthorizationTest {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext ctx;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Value("${security.oauth2.client.client-id}")
	private String CLIENT_ID;
	
	@Value("${security.oauth2.client.client-secret}")
	private String CLIENT_SECRET;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.addFilter(springSecurityFilterChain)
				.alwaysDo(print())
				.build();
	}
	
	@Test
	public void testObtainAccessToken() throws Exception {
		// given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", CLIENT_ID);
		params.add("username", "cdssw@naver.com");
		params.add("password", "1234");
		params.add("common", "common");
		params.add("scope", "read");
		
		// when
		final MvcResult result = mvc.perform(post("/oauth/token")
				.params(params)
				.with(httpBasic(CLIENT_ID, CLIENT_SECRET))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// then
		String content = result.getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		log.info(jsonParser.parseMap(content).get("access_token").toString());
	}
	
	@Test
	public void testObtainAccessTokenByRefreshToken() throws Exception {
		// given
		MultiValueMap<String, String> p = new LinkedMultiValueMap<>();
		p.add("grant_type", "password");
		p.add("client_id", CLIENT_ID);
		p.add("username", "cdssw@naver.com");
		p.add("password", "1234");
		p.add("common", "common");
		p.add("scope", "read");
		
		final MvcResult r = mvc.perform(post("/oauth/token")
				.params(p)
				.with(httpBasic(CLIENT_ID, CLIENT_SECRET))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String content = r.getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		String refreshToken = jsonParser.parseMap(content).get("refresh_token").toString();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "refresh_token");
		params.add("scope", "read");
		params.add("refresh_token", refreshToken);
		
		// when
		final MvcResult result = mvc.perform(post("/oauth/token")
				.params(params)
				.with(httpBasic(CLIENT_ID, CLIENT_SECRET))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// then
		content = result.getResponse().getContentAsString();
		log.info(jsonParser.parseMap(content).get("access_token").toString());		
	}
}
