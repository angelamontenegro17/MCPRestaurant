package com.uptc.frw.mcprestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Sale (Venta) in the restaurant
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Sale(
    @JsonProperty("id") Long id,
    @JsonProperty("date") String date
) {
}
