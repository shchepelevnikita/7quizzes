package it.sevenbits.quizzes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for web sockets
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Method which configures message broker
     * @param config config for message broker
     */
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Method to register stompo endpoints
     * @param registry - registry with endpoints
     */
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws/rooms").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws/rooms").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws/rooms/{roomId}/join").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws/rooms/{roomId}/join").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws/rooms/{roomId}/game/start").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws/rooms/{roomId}/game/start").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws/rooms/{roomId}/game/question/{questionId}/answer").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws/rooms/{roomId}/game/question/{questionId}/answer").setAllowedOriginPatterns("*").withSockJS();
    }
}
