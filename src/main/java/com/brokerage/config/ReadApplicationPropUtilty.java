package com.brokerage.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ReadApplicationPropUtilty {

	private final Environment environment;

	public ReadApplicationPropUtilty(Environment environment) {
		this.environment = environment;
	}

	public String getPassword() {

		return environment.getProperty("spring.security.user.password");

	}

	public String getUsername() {

		return environment.getProperty("spring.security.user.name");

	}

}
