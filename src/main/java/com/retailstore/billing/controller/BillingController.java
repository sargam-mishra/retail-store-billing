package com.retailstore.billing.controller;

import com.retailstore.billing.entity.Bill;
import com.retailstore.billing.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/discount")
public class BillingController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/calculate")
    public BigDecimal calculateDiscount(@RequestBody Bill bill) {
        return discountService.calculateFinalAmount(bill);
    }

}
