package com.chill.catalog.publishing;

import com.chill.catalog.model.MysteryBag;

public interface MysteryBagObserver {
    /**
     * Called whenever a MysteryBag is published.
     */
    void onPublish(MysteryBag bag);
}
