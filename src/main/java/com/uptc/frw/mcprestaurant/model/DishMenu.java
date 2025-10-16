package com.uptc.frw.mcprestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the relationship between Dishes and Menus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DishMenu(
    @JsonProperty("idMenu") Long menuId,
    @JsonProperty("idDish") Long dishId,
    @JsonProperty("price") Double price,
    @JsonProperty("date") String date
) {
}
