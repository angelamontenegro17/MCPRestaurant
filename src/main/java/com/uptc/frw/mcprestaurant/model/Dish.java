package com.uptc.frw.mcprestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Dish (Plato) in the restaurant
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Dish(
    @JsonProperty("id") Long id,
    @JsonProperty("dishType") String dishType,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description
) {
}
