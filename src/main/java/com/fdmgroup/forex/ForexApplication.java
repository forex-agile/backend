package com.fdmgroup.forex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.fdmgroup.forex.security.RsaKeyProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ForexApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}

}
