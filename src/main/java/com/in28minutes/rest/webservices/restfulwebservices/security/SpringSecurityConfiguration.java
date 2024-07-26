package com.in28minutes.rest.webservices.restfulwebservices.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		//Ensuring all requests are authenticated
		http.authorizeHttpRequests(
				auth -> auth.anyRequest().authenticated()
				);
		
		//If a request is not authenticated, a web page is shown
		http.httpBasic(withDefaults());
		
		//Disable CSRF to enable POST & PUT requests
		http.csrf().disable();
		
		return http.build();
	}
}










