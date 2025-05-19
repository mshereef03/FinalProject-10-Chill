package com.chill.catalog.controller;

import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.service.MysteryBagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs/mystery-bags")
public class MysteryBagController {

    private final MysteryBagService mysteryBagService;
    public MysteryBagController(MysteryBagService mysteryBagService) {
        this.mysteryBagService = mysteryBagService;
    }

    @GetMapping
    public List<MysteryBag> getAllMysteryBags() {
        return mysteryBagService.getAllMysteryBags();
    }

    @GetMapping("/published")
    public List<MysteryBag> getPublishedMysteryBags() {
        return mysteryBagService.getPublishedMysteryBags();
    }

    @GetMapping("/{id}")
    public MysteryBag getMysteryBagById(@PathVariable String id) {
        return mysteryBagService.getMysteryBagById(id);
    }

    @PostMapping("/create")
    public MysteryBag createMysteryBag(@RequestBody MysteryBag bag) {
        return mysteryBagService.createMysteryBag(bag);
    }

    @PutMapping("/{id}")
    public MysteryBag updateMysteryBag(
            @PathVariable String id,
            @RequestBody MysteryBag updatedBag
    ) {
        return mysteryBagService.updateMysteryBag(id, updatedBag);
    }

    @DeleteMapping("/{id}")
    public void deleteMysteryBag(@PathVariable String id) {
        mysteryBagService.deleteMysteryBag(id);
    }

    @DeleteMapping
    public void deleteMysteryBags() {
        mysteryBagService.deleteMysteryBags();
    }

    @PostMapping("/publish/{id}")
    public MysteryBag publishMysteryBag(@PathVariable String id) {
        return mysteryBagService.publishMysteryBag(id);
    }

    @PostMapping("/update_quantity/{id}")
    public double updateMysteryBagQuantity(
            @PathVariable String id,
            @RequestParam int quantity
    ) {
        return mysteryBagService.updateMysteryBagQuantity(id, quantity);
    }
}