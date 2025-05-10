package com.chill.catalog.repository;

import com.chill.catalog.model.MysteryBag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface MysteryBagRepository extends MongoRepository<MysteryBag, String> {

    /* activate bags whose releaseAt ≤ now */
    List<MysteryBag> findByStatusAndReleaseAtBefore(MysteryBag.Status status, Instant now);
}