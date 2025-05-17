package com.chill.catalog.observer;

import com.chill.catalog.model.MysteryBag;

public interface MysteryBagObserver {
    void onPublish(MysteryBag bag);
}
