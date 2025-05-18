package com.chill.order.service;
import com.chill.order.model.PromoCode;
import com.chill.order.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class PromoCodeService {
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    public int getDiscountByPromoCode(String code){
        Optional<PromoCode> promo= promoCodeRepository.findById(code);
        if(promo.isPresent()){
            return promo.get().getDiscount();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PromoCode not found!");
        }
    }

    public PromoCode addPromoCode(PromoCode promoCode){
        return promoCodeRepository.save(promoCode);
    }
    public void deletePromoCode(String code){
         promoCodeRepository.deleteById(code);

    }
    public PromoCode updatePromoCode(PromoCode promoCode){
        return promoCodeRepository.save(promoCode);

    }

}
