package com.chill.order.service.StrategyPattern;

import com.chill.order.model.MysteryBagDTO;
import com.chill.order.model.Order;

import java.util.List;

public class BulkDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Order order, double discount) {
        double price= order.getPrice();
        List<MysteryBagDTO> mysteryBagList = order.getCart().convertJsonToList();
        int items=mysteryBagList.size();
        double totalDiscount;
        switch(items){
            case 1: totalDiscount=0;
            break;
            case 2: totalDiscount=0.5*discount;
            break;
            case 3: totalDiscount=discount;
            break;
            default: totalDiscount=2*discount;
        }
        if(totalDiscount>=price){
            totalDiscount=price/2;
        }
        return price-totalDiscount;
    }
}

