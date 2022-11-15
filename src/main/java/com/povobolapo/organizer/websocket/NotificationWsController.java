package com.povobolapo.organizer.websocket;

import com.povobolapo.organizer.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.AuthenticationException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@ServerEndpoint(
        value = "/notification/ws",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class NotificationWsController {
    private static final Logger log = LoggerFactory.getLogger(NotificationWsController.class);

    private final JwtTokenUtil jwtTokenUtil = SpringContext.getBean(JwtTokenUtil.class);
    private final WsSessionManager sessionManager = SpringContext.getBean(WsSessionManager.class);

    @OnOpen
    public void onOpen(Session session) throws AuthenticationException {
        sessionManager.addSession(authUser(session), session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    @OnClose
    public void onClose(Session session) throws AuthenticationException {
        sessionManager.removeSession(authUser(session));
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws AuthenticationException {
        sessionManager.removeSession(authUser(session));
    }

    private String authUser(Session session) throws AuthenticationException {
        Map<String, List<String>> params = session.getRequestParameterMap();

        if (params == null) {
            throw new AuthenticationException("Incorrect token");
        }

        if (!params.containsKey("token")) {
            throw new AuthenticationException("Incorrect token");
        }
        List<String> tokenValues = params.get("token");

        if (tokenValues == null || tokenValues.isEmpty()) {
            throw new AuthenticationException("Incorrect token");
        }
        String token = tokenValues.get(0);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("Incorrect token");
        }

        UserDetails userDetails = jwtTokenUtil.validateToken(token);
        log.info("Authenticated user {}", userDetails.getUsername());
        return userDetails.getUsername();
    }
}
