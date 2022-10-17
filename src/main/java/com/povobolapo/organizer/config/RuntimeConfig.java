package com.povobolapo.organizer.config;

import com.povobolapo.organizer.utils.EventDispatcher;
import com.povobolapo.organizer.utils.EventHandler;
import com.povobolapo.organizer.websocket.WsSessionManager;
import com.povobolapo.organizer.websocket.model.NotificationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RuntimeConfig {
    @Bean
    @Scope("singleton")
    public EventDispatcher eventDispatcher(WsSessionManager sessionManager) {
        return new EventDispatcher() {{
            registerHandler(NotificationMessage.class, (EventHandler<NotificationMessage>) sessionManager::onEvent);
        }};
    }
}
