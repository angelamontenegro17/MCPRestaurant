package com.uptc.frw.mcprestaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Menu in the restaurant
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Menu(
    @JsonProperty("idMenu") Long id,
    @JsonProperty("description") String description
) {
}
