package com.makernav.categorize.service;

public enum ItemFilterType {
    NAME("name"),
    CATEGORY("category"),
    STATUS("status"),
    QUANTITY("quantity"),
    TYPE("type");

    private final String VALUE;

    ItemFilterType(String value) {
        this.VALUE = value;
    }
}
