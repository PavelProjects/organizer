package com.povobolapo.organizer.utils;

public interface EventHandler<T extends Event>{
    void onEvent(T event) throws Exception;
}
