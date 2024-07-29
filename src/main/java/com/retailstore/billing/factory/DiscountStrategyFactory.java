package com.retailstore.billing.factory;

import com.retailstore.billing.enums.CustomerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class DiscountStrategyFactory {

    private final Map<CustomerType, DiscountStrategy> strategies = new EnumMap<CustomerType, DiscountStrategy>(CustomerType.class);

    @Autowired
    public DiscountStrategyFactory(EmployeeDiscountStrategy employeeStrategy,
                                   AffiliateDiscountStrategy affiliateStrategy,
                                   RegularCustomerDiscountStrategy regularCustomerStrategy) {
        strategies.put(CustomerType.EMPLOYEE, employeeStrategy);
        strategies.put(CustomerType.AFFILIATE, affiliateStrategy);
        strategies.put(CustomerType.REGULAR, regularCustomerStrategy);
    }

    public DiscountStrategy getStrategy(CustomerType customerType) {
        return strategies.get(customerType);
    }
}
