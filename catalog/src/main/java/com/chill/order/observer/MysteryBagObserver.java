package com.chill.order.observer;

import com.chill.order.model.MysteryBag;

public interface MysteryBagObserver {
    void onPublish(MysteryBag bag);
}
