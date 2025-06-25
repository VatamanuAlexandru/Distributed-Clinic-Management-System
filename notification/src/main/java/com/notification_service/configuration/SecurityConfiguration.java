package com.notification_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.notification_service.service.NotificationService;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/notification/**").permitAll().anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public NotificationService notificationService() {
		return new NotificationService();
	}
}
