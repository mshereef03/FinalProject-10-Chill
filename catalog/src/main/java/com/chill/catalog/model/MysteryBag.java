package com.chill.catalog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "mystery_bags")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class MysteryBag {
    @Id
    private String id;
    private List<String> itemIds;
    private double basePrice;
    private Instant releaseAt;
    private Status status = Status.PENDING;
    private Size size;

    public enum Status   { PENDING, ACTIVE, SOLD_OUT }
    public enum Size { SMALL, MEDIUM, BIG }

}
