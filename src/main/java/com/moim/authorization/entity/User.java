package com.moim.authorization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User.java
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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 코드 내에서 객체를 생성하지 못하도록
@Getter
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, unique = true, nullable = false)
    private String username;
	
    @Column(length = 400, nullable = false)
    private String password;
    
    @Column(nullable = false)
    private int userType;
    
	@Builder
	public User(String username, String password, int userType) {
		this.username = username;
		this.password = password;
		this.userType = userType;
	}
}
