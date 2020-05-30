package com.moim.authorization.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {

	private String grant_type;
	private String username;
	private String password;
	private String common;
	
	@Builder
	public LoginDto(String grant_type, String username, String password, String common) {
		this.grant_type = grant_type;
		this.username = username;
		this.password = password;
		this.common = common;
	}
}
