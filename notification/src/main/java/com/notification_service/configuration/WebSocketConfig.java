package com.notification_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); // Activează topicurile pentru subscrieri
		config.setApplicationDestinationPrefixes("/app"); // Prefix pentru mesaje către @MessageMapping
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/notifications").setAllowedOrigins("http://localhost:4200") // Permite CORS pentru
																								// Angular
		; // Suport SockJS (opțional, dacă vrei compatibilitate)
	}
}
