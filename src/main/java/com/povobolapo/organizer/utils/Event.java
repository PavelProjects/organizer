package com.povobolapo.organizer.utils;

public interface Event {
    default Class<?> getType() {
        return getClass();
    }
}
