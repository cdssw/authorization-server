package com.moim.authorization.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
    
	private String userNm;
	
	private String userNickNm;
	
	private String phone;
	
	private String mainTalent;
	
	private String talent;
	
	private String interest;
	
	private String avatarPath;
	
	@Embedded
	private Policy policy;
	
	@Builder
	public User(String username
			, String password
			, int userType
			, String userNm
			, String userNickNm
			, String phone
			, String mainTalent
			, String talent
			, String interest
			, String avatarPath
			, Policy policy
			) {
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.userNm = userNm;
		this.userNickNm = userNickNm;
		this.phone = phone;
		this.mainTalent = mainTalent;
		this.talent = talent;
		this.interest = interest;
		this.avatarPath = avatarPath;
		this.policy = policy;
	}
}
