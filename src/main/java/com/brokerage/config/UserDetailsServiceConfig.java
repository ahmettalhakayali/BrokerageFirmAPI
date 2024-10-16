package com.brokerage.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brokerage.model.SecurityProperties;

@Configuration
public class UserDetailsServiceConfig {

	@Autowired
	private SecurityProperties securityProperties;

	// Custom UserDetailsService to load users from properties
	@Bean(name = "customUserDetailsService")
	public UserDetailsService customUserDetailsService() {
		return username -> {
			if (username.equals(securityProperties.getAdmin().getUsername())) {
				return new User(securityProperties.getAdmin().getUsername(),
						passwordEncoder().encode(securityProperties.getAdmin().getPassword()),
						List.of(() -> "ROLE_ADMIN"));
			} else if (username.equals(securityProperties.getCustomer().getUsername())) {
				return new User(securityProperties.getCustomer().getUsername(),
						passwordEncoder().encode(securityProperties.getCustomer().getPassword()),
						List.of(() -> "ROLE_CUSTOMER"));
			} else {
				throw new UsernameNotFoundException("User not found");
			}
		};
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
