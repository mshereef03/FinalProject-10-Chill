package com.chill.catalog.service;

import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.publishing.MysteryBagPublisher;
import com.chill.catalog.repository.MysteryBagRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MysteryBagService {
    private final MysteryBagRepository repo;
    private final MysteryBagPublisher publisher;

    public MysteryBagService(MysteryBagRepository repo,
                             MysteryBagPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    /**
     * Mark the bag ACTIVE and notify observers.
     */
    public MysteryBag publishMysteryBag(String id) {
        MysteryBag bag = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bag not found: " + id));

        // Optional: only allow if releaseAt <= now
        if (bag.getReleaseAt().isAfter(Instant.now())) {
            throw new IllegalStateException("Cannot publish before " + bag.getReleaseAt());
        }

        bag.setStatus(MysteryBag.Status.ACTIVE);
        MysteryBag saved = repo.save(bag);

        // fire observers
        publisher.publish(saved);

        return saved;
    }
}
