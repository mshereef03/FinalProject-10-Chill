package com.chill.order.client;

import com.chill.order.model.MysteryBagDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mysterybag-service", url = "http://mysterybag-service/api/mysterybags")
public interface MysteryBagClient {

    @GetMapping("/{id}")
    MysteryBagDTO getMysteryBag(@PathVariable("id") Long mysteryBagId);
}
