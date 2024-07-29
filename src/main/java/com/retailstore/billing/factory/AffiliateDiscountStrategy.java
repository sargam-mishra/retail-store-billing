package com.retailstore.billing.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AffiliateDiscountStrategy implements DiscountStrategy{

    private BigDecimal affiliateDiscount= new BigDecimal(0.10);

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(affiliateDiscount);
    }
}
