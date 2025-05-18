package com.chill.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog", url = "http://catalog/update_quantity/{id}")
public interface MysteryBagClient {

    @PostMapping("/{id}")
    double getMysteryBag(@PathVariable("id") String mysteryBagId);
}
