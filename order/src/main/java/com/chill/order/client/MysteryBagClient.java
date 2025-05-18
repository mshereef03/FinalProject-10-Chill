package com.chill.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient( name = "catalog", url  = "http://catalog_service:8080")
public interface MysteryBagClient {

    @PostMapping("/catalog/mystery-bags/update_quantity/{id}")
    double getMysteryBag(@PathVariable("id") String mysteryBagId);
}
