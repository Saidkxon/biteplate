package com.biteplate.pattern.observer;

public interface NotificationSubject {
    void attach(SystemObserver observer);
    void detach(SystemObserver observer);
    void notifyObservers(String eventType, String message);
}
