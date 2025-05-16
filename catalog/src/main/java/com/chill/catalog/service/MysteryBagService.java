package com.chill.catalog.service;

import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.observer.MysteryBagObserver;
import com.chill.catalog.repository.MysteryBagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class MysteryBagService {
    private final MysteryBagRepository mysteryBagRepository;
    private final List<MysteryBagObserver> observers;  // Spring injects your MenuItemService here

    public MysteryBagService(
            MysteryBagRepository mysteryBagRepository,
            List<MysteryBagObserver> observers
    ) {
        this.mysteryBagRepository    = mysteryBagRepository;
        this.observers  = observers;
    }

    public List<MysteryBag> getAllMysteryBags() {
        return mysteryBagRepository.findAll();
    }

    public MysteryBag getMysteryBagById(String id) {
        return mysteryBagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "MysteryBag not found with id “" + id + "”"
                ));
    }

    public MysteryBag updateMysteryBag(String id, MysteryBag updatedBag) {
        MysteryBag bag = getMysteryBagById(id);
        bag.setItemIds(updatedBag.getItemIds());
        bag.setBasePrice(updatedBag.getBasePrice());
        bag.setReleaseAt(updatedBag.getReleaseAt());
        bag.setSize(updatedBag.getSize());
        return mysteryBagRepository.save(bag);
    }

    public void deleteMysteryBag(String id) {
        mysteryBagRepository.deleteById(id);
    }

    public MysteryBag publishMysteryBag(String id) {
        MysteryBag bag = getMysteryBagById(id);
        bag.setStatus(MysteryBag.Status.ACTIVE);
        bag.setReleaseAt(Instant.now());

        // notify all observers (MenuItemService will run onPublish)
        observers.forEach(o -> o.onPublish(bag));

        return mysteryBagRepository.save(bag);
    }

    public MysteryBag updateMysteryBagQuantity(String id, int quantity) {
        MysteryBag bag = getMysteryBagById(id);
        int newQuantity = bag.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough items in stock"
            );
        }
        if(newQuantity == 0) {
            bag.setStatus(MysteryBag.Status.SOLD_OUT);
        }
        bag.setQuantity(newQuantity);
        mysteryBagRepository.save(bag);

        return bag;
    }
}
