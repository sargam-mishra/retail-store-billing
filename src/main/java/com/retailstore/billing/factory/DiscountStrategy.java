package com.retailstore.billing.factory;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal applyDiscount(BigDecimal amount);

}
