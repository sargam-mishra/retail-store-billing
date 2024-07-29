package com.retailstore.billing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class Bill {

    private Long billId;

    private Long customerId;

    private List<Product> purchasedProducts;

}
