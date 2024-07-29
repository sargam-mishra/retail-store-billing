package com.retailstore.billing.enums;

public enum ProductCategory {

    GROCERY,
    NON_GROCERY;

    private String value;


    @Override
    public String toString() {

        return value;
    }
}
