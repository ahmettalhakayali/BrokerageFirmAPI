package com.brokerage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;

	// Configure authentication provider
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider);
	}

	// Password encoder bean
	@Bean(name = "customPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Primary
	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authz -> authz.requestMatchers("/login").permitAll() // Allow
																														// login
																														// without
																														// authentication
				.requestMatchers("/admin/**").hasRole("ADMIN") // Only Admins can access /admin
				.requestMatchers("/customer/**").hasRole("CUSTOMER") // Only Customers can access /customer
				.anyRequest().authenticated() // Any other requests must be authenticated
		).httpBasic(); // Basic Authentication enabled
		return http.build();
	}
}
