package com.chill.order.repository;

import com.chill.order.model.MysteryBag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MysteryBagRepository extends MongoRepository<MysteryBag, String> {

}