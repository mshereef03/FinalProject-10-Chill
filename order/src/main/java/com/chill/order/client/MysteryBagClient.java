package com.chill.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "catalog", url = "http://catalog_service:8080")
//public interface MysteryBagClient {
//
//    @PostMapping("/catalog/mystery-bags/update_quantity/{id}")
//    double getMysteryBag(@PathVariable("id") String mysteryBagId, @RequestParam("quantity") int quantity);
//}
@FeignClient(name = "catalog-service", url = "${catalog.service.url}",path = "/catalogs/mystery-bags")
public interface MysteryBagClient {
    @PostMapping("/update_quantity/{id}")
    double getMysteryBag(@PathVariable("id") String id, @RequestParam("quantity") int quantity);
}