package com.retailstore.billing.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmployeeDiscountStrategy implements DiscountStrategy{

    private BigDecimal employeeDiscount=new BigDecimal(0.3);

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(employeeDiscount);
    }
}
