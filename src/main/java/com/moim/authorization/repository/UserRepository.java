package com.moim.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moim.authorization.entity.User;

/**
 * UserRepository.java
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
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
}
