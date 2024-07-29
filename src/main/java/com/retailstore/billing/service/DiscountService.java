package com.retailstore.billing.service;

import com.retailstore.billing.entity.Bill;
import com.retailstore.billing.entity.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface DiscountService {

    BigDecimal calculateFinalAmount(Bill bill);

}
