package com.povobolapo.organizer.utils;

import com.povobolapo.organizer.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventDispatcher {
    private static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);

    private Map<Class<?>, Set<EventHandler<Event>>> handlers;

    public EventDispatcher() {
        this.handlers = new ConcurrentHashMap<>();
    }

    public void registerHandler(Class<? extends Event> type, EventHandler<? extends Event> handler) {
        handlers.putIfAbsent(type, new CopyOnWriteArraySet<>());
        handlers.get(type).add((EventHandler<Event>) handler);
        log.info("Added new event handler for class {}:: {}", type.getName(), handler.getClass().getName());
    }

    public void dispatch(Event event) throws Exception{
        log.info("Dispatching event {}", event);
        Set<EventHandler<Event>> handler = handlers.get(event.getType());

        if (handler != null) {
            for (EventHandler<Event> h : handler) {
                h.onEvent(event);
            }
        } else {
            log.warn("Not handlers found!");
        }
    }

}
