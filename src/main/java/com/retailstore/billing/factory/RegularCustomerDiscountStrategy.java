package com.retailstore.billing.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;


@Component
public class RegularCustomerDiscountStrategy implements DiscountStrategy{

    private BigDecimal regularDiscount = new BigDecimal(0.05);

    private int yearsForRegularDiscount = 2;

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        // This assumes that the discount is only applicable if the customer has been a customer for more than 2 years
        return amount.multiply(regularDiscount);
    }

    public boolean isEligible(LocalDate joinDate) {
        return Period.between(joinDate, LocalDate.now()).getYears() > yearsForRegularDiscount;
    }
}
