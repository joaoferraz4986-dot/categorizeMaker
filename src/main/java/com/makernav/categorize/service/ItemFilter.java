package com.makernav.categorize.service;

public record ItemFilter(
    ItemFilterType type,
    String filter
){ }
