package com.clinic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/patient/**", "/department/**", "/doctor/**", "/person/**", "/services/**",
								"/doctorSchedule/**", "/appointment/**", "/medical-history/**", "/patients/**",
								"/diagnosis/**", "/treatment/**", "/medication/**")
						.permitAll().anyRequest().authenticated());
		return http.build();
	}
}
