package com.uptc.frw.mcprestaurant.service;

import com.uptc.frw.mcprestaurant.model.Dish;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Service for managing restaurant dishes
 */
@Service
public class DishService {

    private final RestClient restClient;

    public DishService(@Value("${restaurant.api.base-url:http://localhost:8080/api}") String baseUrl,
                      @Value("${restaurant.api.username:admin}") String username,
                      @Value("${restaurant.api.password:password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    /**
     * Get all dishes from the restaurant
     * @return List of all dishes
     */
    @Tool(description = "Get all dishes from the restaurant")
    public List<Dish> getAllDishes() {
        return restClient.get()
                .uri("/dishes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Dish>>() {});
    }

    /**
     * Get a specific dish by ID
     * @param id The dish ID
     * @return The dish details
     */
    @Tool(description = "Get a specific dish by its ID")
    public Dish getDishById(Long id) {
        return restClient.get()
                .uri("/dishes/{id}", id)
                .retrieve()
                .body(Dish.class);
    }

    /**
     * Create a new dish
     * @param type The dish type
     * @param name The dish name
     * @param description The dish description
     * @return The created dish
     */
    @Tool(description = "Create a new dish with type, name, and description")
    public Dish createDish(String dishType, String name, String description) {
        return restClient.post()
                .uri("/dishes")
                .body(Map.of(
                    "dishType", dishType,
                    "name", name,
                    "description", description
                ))
                .retrieve()
                .body(Dish.class);
    }

    /**
     * Update an existing dish
     * @param id The dish ID
     * @param type The new type
     * @param name The new name
     * @param description The new description
     * @return The updated dish
     */
    @Tool(description = "Update an existing dish's information")
    public Dish updateDish(Long id, String dishType, String name, String description) {
        return restClient.put()
                .uri("/dishes")
                .body(Map.of(
                    "id", id,
                    "dishType", dishType,
                    "name", name,
                    "description", description
                ))
                .retrieve()
                .body(Dish.class);
    }

    /**
     * Delete a dish
     * @param id The dish ID to delete
     * @return Success message
     */
    @Tool(description = "Delete a dish by its ID")
    public String deleteDish(Long id) {
        restClient.delete()
                .uri("/dishes?id={id}", id)
                .retrieve()
                .toBodilessEntity();
        return "Dish deleted successfully";
    }
}
