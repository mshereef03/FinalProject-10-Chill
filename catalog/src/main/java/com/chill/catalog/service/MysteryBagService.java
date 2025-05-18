package com.chill.catalog.service;

import com.chill.catalog.model.MenuItem;
import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.observer.MysteryBagObserver;
import com.chill.catalog.pricing.PricingEngine;
import com.chill.catalog.repository.MysteryBagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MysteryBagService {
    private final MysteryBagRepository mysteryBagRepository;
    private final MenuItemService menuItemService;
    private final MysteryBagObserver observer;
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

    public List<MysteryBag> getPublishedMysteryBags() {
        List<MysteryBag> mysteryBags = mysteryBagRepository.findAll();
        return mysteryBags.stream().filter(mysteryBag -> mysteryBag.getStatus()== MysteryBag.Status.ACTIVE).toList();
    }

    public MysteryBag getMysteryBagById(String id) {
        return mysteryBagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "MysteryBag not found with id “" + id + "”"
                ));
    }

    private double getBagPrice(MysteryBag bag){
        double price = 0;
        for(String id: bag.getItemIds()) {
            Optional<MenuItem> menuItem = menuItemService.getMenuItemById(id);
            if(menuItem.isEmpty())
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Menu item does not exist"
                );

            double menuItemPrice = menuItem.get().getPrice();
            price+= menuItemPrice;
        }
        return price;
    }

    public MysteryBag createMysteryBag(MysteryBag newBag) {
        if(newBag.getQuantity() <= 0)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mystery bag quantity should be more than 0"
            );

        newBag.setPrice(getBagPrice(newBag));

        try {
            double finalPrice = pricingEngine.calculatePrice(newBag);
            newBag.setPrice(finalPrice);
            return mysteryBagRepository.save(newBag);
        }
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This strategy does not exist"
            );
        }
    }

    public MysteryBag updateMysteryBag(String id, MysteryBag updatedBag) {
        MysteryBag bag = getMysteryBagById(id);
        bag.setItemIds(updatedBag.getItemIds());
        bag.setPrice(updatedBag.getPrice());
        bag.setSize(updatedBag.getSize());
        return mysteryBagRepository.save(bag);
    }

    public void deleteMysteryBag(String id) {
        mysteryBagRepository.deleteById(id);
    }

    public void deleteMysteryBags() {
        mysteryBagRepository.deleteAll();
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
        if(newQuantity > 0) {
            bag.setStatus(MysteryBag.Status.ACTIVE);
        }
        bag.setQuantity(newQuantity);
        mysteryBagRepository.save(bag);

        return bag.getPrice();
    }
}
