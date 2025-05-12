package com.chill.catalog.controller;

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