package com.chill.catalog.publishing;

import com.chill.catalog.model.MysteryBag;

import java.util.List;

@Component
public class MysteryBagPublisher {
    private final List<MysteryBagObserver> observers;

    public MysteryBagPublisher(List<MysteryBagObserver> observers) {
        this.observers = observers;
    }

    /**
     * Notify all registered observers that this bag was published.
     */
    public void publish(MysteryBag bag) {
        observers.forEach(obs -> obs.onPublish(bag));
    }
}
