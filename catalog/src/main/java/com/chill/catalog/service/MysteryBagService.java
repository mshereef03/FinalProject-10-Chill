package com.chill.catalog.service;

import com.chill.catalog.model.MenuItem;
import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.observer.MysteryBagObserver;
import com.chill.catalog.pricing.PricingEngine;
import com.chill.catalog.repository.MysteryBagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MysteryBagService {
    private final MysteryBagRepository mysteryBagRepository;
    private final MenuItemService menuItemService;
    private final MysteryBagObserver observer;  // Spring injects your MenuItemService here
    private final PricingEngine pricingEngine;

    public MysteryBagService(
            MysteryBagRepository mysteryBagRepository,
            MenuItemService menuItemService,
            MysteryBagObserver observer,
            PricingEngine pricingEngine
    ) {
        this.mysteryBagRepository    = mysteryBagRepository;
        this.menuItemService = menuItemService;
        this.observer  = observer;
        this.pricingEngine = pricingEngine;
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

    public MysteryBag createMysteryBag(MysteryBag newBag) {
        for(String id: newBag.getItemIds()) {
            Optional<MenuItem> menuItem = menuItemService.getMenuItemById(id);
            if(menuItem.isEmpty())
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Menu item does not exist"
                 );

            double menuItemPrice = menuItem.get().getPrice();
            double bagBasePrice = newBag.getBasePrice();
            newBag.setBasePrice(bagBasePrice + menuItemPrice);
        }
        double finalPrice = pricingEngine.calculatePrice(newBag);
        newBag.setBasePrice(finalPrice);

        return mysteryBagRepository.save(newBag);
    }


    public MysteryBag updateMysteryBag(String id, MysteryBag updatedBag) {
        MysteryBag bag = getMysteryBagById(id);
        bag.setItemIds(updatedBag.getItemIds());
        bag.setBasePrice(updatedBag.getBasePrice());
        bag.setSize(updatedBag.getSize());
        return mysteryBagRepository.save(bag);
    }

    public void deleteMysteryBag(String id) {
        mysteryBagRepository.deleteById(id);
    }

    public MysteryBag publishMysteryBag(String id) {
        MysteryBag bag = getMysteryBagById(id);
        bag.setStatus(MysteryBag.Status.ACTIVE);

        // notify all observers (MenuItemService will run onPublish)
        observer.onPublish(bag);

        return mysteryBagRepository.save(bag);
    }

    public double updateMysteryBagQuantity(String id, int quantity) {
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

        return bag.getBasePrice();
    }
}
