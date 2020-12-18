package com.spiegelberger.phototapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	Environment env;

	@Autowired
	public WebSecurity(Environment env) {
		this.env = env;
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**")
		/*
		Only Zuul should have access but this line always ends up with "Access forbidden",
		which is a well-known Windows problem:
		*/
		//.hasIpAddress(env.getProperty("gateway.ip"))
		.permitAll();
		http.headers().frameOptions().disable();
	}
}
