package com.masterangler.mock;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private List<Object> listeners = new ArrayList<>();

    public void register(Object listener) {
        listeners.add(listener);
        System.out.println("[EventBus] Registered listener: " + listener.getClass().getSimpleName());
    }
}
