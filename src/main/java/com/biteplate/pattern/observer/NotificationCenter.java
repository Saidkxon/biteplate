package com.biteplate.pattern.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationCenter implements NotificationSubject {
    private final List<SystemObserver> observers = new ArrayList<>();

    public NotificationCenter(List<SystemObserver> startupObservers) {
        observers.addAll(startupObservers);
    }

    public void attach(SystemObserver observer) {
        observers.add(observer);
    }

    public void detach(SystemObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventType, String message) {
        for (SystemObserver observer : observers) {
            observer.update(eventType, message);
        }
    }
}
