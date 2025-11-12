package com.crus.Inventory_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
    BAKERY,
    BBQ,
    @JsonProperty("BEER WINE")
    BEER_WINE,
    COSMETICS,
    DAIRY,
    @JsonProperty("FROZEN FOODS")
    FROZEN_FOODS,
    GROCERY,
    @JsonProperty("JUICE & COCKTAIL")
    JUICE_COCKTAIL,
    MEAT,
    MEDICINE,
    @JsonProperty("NON FOOD")
    NON_FOOD,
    @JsonProperty("ONLINE LOTTO")
    ONLINE_LOTTO,
    @JsonProperty("PREPARED FOOD")
    PREPARED_FOOD,
    PRODUCE,
    @JsonProperty("SCRATCH OFFS")
    SCRATCH_OFFS,
    SEAFOOD,
    @JsonProperty("SOFT DRINKS")
    SOFT_DRINKS,
    @JsonProperty("ENERGY DRINKS")
    ENERGY_DRINKS,
    TOBACCO,
    @JsonProperty("HOT FOOD")
    HOT_FOOD,
    ICECREAM,
    @JsonProperty("FASHION WEAR")
    FASHION_WEAR,
    KITCHEN,
    LIQUOR,
    @JsonProperty("FROZEN YOGURT")
    FROZEN_YOGURT,
    @JsonProperty("DEPARTMENT NOT ON FILE")
    DEPARTMENT_NOT_ON_FILE;
}


