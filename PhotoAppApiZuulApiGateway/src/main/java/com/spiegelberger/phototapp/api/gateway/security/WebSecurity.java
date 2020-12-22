package com.spiegelberger.phototapp.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private final Environment env;

    @Autowired
    public WebSecurity(Environment env) {
        this.env = env;
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable(); 
    	http.headers().frameOptions().disable();
    	
    	http.authorizeRequests()
    	.antMatchers(env.getProperty("api.users.actuator.url.path")).permitAll()
    	.antMatchers(env.getProperty("api.zuul.actuator.url.path")).permitAll()
    	.antMatchers(env.getProperty("api.h2console.url.path")).permitAll()
    	.antMatchers(HttpMethod.POST, env.getProperty("api.registration.url.path")).permitAll()
    	.antMatchers(HttpMethod.POST, env.getProperty("api.login.url.path")).permitAll()
    	.anyRequest().authenticated()
    	.and()
    	.addFilter(new AuthorizationFilter(authenticationManager(), env));
    	
    	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	
    }	
    
}
