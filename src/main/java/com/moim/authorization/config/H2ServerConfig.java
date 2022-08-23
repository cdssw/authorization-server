package com.moim.authorization.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("test")
public class H2ServerConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public DataSource dataSource() throws SQLException {
		Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8096").start();
		
		return new HikariDataSource();
	}
}
