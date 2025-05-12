package com.chill.catalog.controller;

import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.service.MysteryBagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/mystery-bags")
public class MysteryBagController {

    private final MysteryBagService service;

    public MysteryBagController(MysteryBagService service) {
        this.service = service;
    }


    @PostMapping("/publish/{id}")
    public ResponseEntity<MysteryBag> publishMysteryBag(@PathVariable String id) {
        MysteryBag published = service.publishMysteryBag(id);
        return ResponseEntity.ok(published);
    }
}