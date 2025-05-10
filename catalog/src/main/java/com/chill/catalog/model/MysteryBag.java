package com.chill.catalog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "mystery_bags")
public class MysteryBag {
    @Id
    private String id;
    private List<String> itemIds;
    private double basePrice;
    private Instant releaseAt;
    private Instant expiresAt;
    private Status status = Status.PENDING;

    public enum Status   { PENDING, ACTIVE, SOLD_OUT }

}
