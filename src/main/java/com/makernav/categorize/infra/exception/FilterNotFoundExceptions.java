package com.makernav.categorize.infra.exception;

public class FilterNotFoundExceptions extends RuntimeException {
    public FilterNotFoundExceptions(String message) {
        super(message);
    }
}
