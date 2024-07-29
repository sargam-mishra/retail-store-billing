package com.retailstore.billing.enums;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CustomerType {

    EMPLOYEE("EMPLOYEE"), AFFILIATE("AFFILIATE"), REGULAR("REGULAR");

    private final String value;


    @Override
    public String toString() {

        return value;
    }

}
