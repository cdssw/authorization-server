package com.moim.authorization.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Policy.java
 * 
 * @author cdssw
 * @since 2020. 10. 22.
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * 2020. 10. 22.    cdssw            최초 생성
 * </pre>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 시간자동 업데이트를 위해서 추가
public class Policy {

	@Column(nullable = false)
	private boolean serviceYn;
	
	@Column(nullable = false)
	private boolean privateYn;
	
	@Column(nullable = false)
	private boolean profileYn;
	
	@CreatedDate // 자동 생성시간
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime policyDt;	
	
	@Builder
	public Policy(boolean serviceYn, boolean privateYn, boolean profileYn) {
		this.serviceYn = serviceYn;
		this.privateYn = privateYn;
		this.profileYn = profileYn;
	}	
}
