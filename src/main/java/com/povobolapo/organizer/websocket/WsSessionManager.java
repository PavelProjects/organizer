package com.povobolapo.organizer.websocket;

import com.povobolapo.organizer.websocket.model.NotificationMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class WsSessionManager {
    private static final int DEFAULT_TIMEOUT_MILLS = 30 * 60 * 1000;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final String PING = "PING";
    private static final int DEFAULT_DELAY = 0;
    private static final int DEFAULT_PERIOD = 120;

    private static final Logger log = LoggerFactory.getLogger(WsSessionManager.class);
    private final ConcurrentHashMap<String, Session> activeSessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public WsSessionManager() {
        executor.scheduleAtFixedRate(pingBroadcast(), DEFAULT_DELAY, DEFAULT_PERIOD, TimeUnit.SECONDS);
    }

    public void addSession(String login, Session session) {
        setSessionProperties(session);
        activeSessions.putIfAbsent(login, session);
        log.info("Added new session for user {}", login);
    }

    public void removeSession(String login) {
        Session session = activeSessions.remove(login);
        if (null == session) {
            return;
        }
        try {
            session.close();
        } catch (Exception ex) {
            log.error("Failed to close session", ex);
        }
        log.info("User's {} session was removed", login);
    }

    public void sendObject(String login, Object obj) {
        Session session = activeSessions.get(login);
        if (null == session) {
            return;
        }
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendObject(obj);
            } else {
                log.warn("User {} session is dead, removing", login);
                activeSessions.remove(login);
            }
        } catch (EncodeException | IOException exc) {
            log.error("Failed to send object to user {}", login, exc);
        }
    }

    private void setSessionProperties(Session session) {
            session.setMaxBinaryMessageBufferSize(DEFAULT_BUFFER_SIZE);
            session.setMaxTextMessageBufferSize(DEFAULT_BUFFER_SIZE);
            session.setMaxIdleTimeout(DEFAULT_TIMEOUT_MILLS);
    }

    private Runnable pingBroadcast() {
        return () -> {
            for (Map.Entry<String, Session> entry : activeSessions.entrySet()) {
                try {
                    if (entry.getValue().isOpen()) {
                        entry.getValue().getBasicRemote().sendPing(ByteBuffer.wrap(PING.getBytes()));
                    } else {
                        activeSessions.remove(entry.getKey());
                    }
                } catch (IOException e) {
                    log.error("Failed to send a ping request to user {} session", entry.getKey(), e);
                }
            }
        };
    }

    public void onEvent(NotificationMessage event) throws Exception {
        String login = event.getUserTo().getLogin();
        if (StringUtils.isNotBlank(login)) {
            log.debug("Sending notification for user {}", login);
            sendObject(login, event);
        }
    }
}
