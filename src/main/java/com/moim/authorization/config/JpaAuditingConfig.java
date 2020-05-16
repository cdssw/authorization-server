package com.moim.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaAuditingConfig.java
 * 
 * @author cdssw
 * @since Apr 18, 2020
 * @description JPA LocalDateTime 자동 업데이트, BaseTimeEntity 참고
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 18, 2020   cdssw            최초 생성
 * </pre>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
