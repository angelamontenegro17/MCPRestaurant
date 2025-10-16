package com.uptc.frw.mcprestaurant.service;

import com.uptc.frw.mcprestaurant.model.DishMenu;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Service for managing dish-menu relationships
 */
@Service
public class DishMenuService {

    private final RestClient restClient;

    public DishMenuService(@Value("${restaurant.api.base-url:http://localhost:8080/api}") String baseUrl,
                          @Value("${restaurant.api.username:admin}") String username,
                          @Value("${restaurant.api.password:password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    /**
     * Get all dish-menu relationships
     * @return List of all dish-menu relationships
     */
    @Tool(description = "Get all dish-menu relationships")
    public List<DishMenu> getAllDishMenus() {
        return restClient.get()
                .uri("/dish-menus")
                .retrieve()
                .body(new ParameterizedTypeReference<List<DishMenu>>() {});
    }

    /**
     * Get a specific dish-menu relationship
     * @param idMenu The menu ID
     * @param iddish The dish ID
     * @return The dish-menu relationship
     */
    @Tool(description = "Get a specific dish-menu relationship")
    public DishMenu getDishMenu(Long idMenu, Long iddish) {
        return restClient.get()
                .uri("/dish-menus/{idMenu}/{iddish}", idMenu, iddish)
                .retrieve()
                .body(DishMenu.class);
    }

    /**
     * Add a dish to a menu
     * @param idMenu The menu ID
     * @param idDish The dish ID
     * @param price The price of the dish in this menu
     * @param date The date this dish was added to the menu
     * @return The created relationship
     */
    @Tool(description = "Add a dish to a menu with price and date")
    public DishMenu addDishToMenu(Long idMenu, Long idDish, Double price, String date) {
        return restClient.post()
                .uri("/dish-menus")
                .body(Map.of(
                    "idMenu", idMenu,
                    "idDish", idDish,
                    "price", price,
                    "date", date
                ))
                .retrieve()
                .body(DishMenu.class);
    }

    /**
     * Update a dish-menu relationship
     * @param idMenu The menu ID
     * @param idDish The dish ID
     * @param price The new price
     * @param date The new date
     * @return The updated relationship
     */
    @Tool(description = "Update a dish-menu relationship (price and/or date)")
    public DishMenu updateDishMenu(Long idMenu, Long idDish, Double price, String date) {
        return restClient.put()
                .uri("/dish-menus")
                .body(Map.of(
                    "idMenu", idMenu,
                    "idDish", idDish,
                    "price", price,
                    "date", date
                ))
                .retrieve()
                .body(DishMenu.class);
    }

    /**
     * Remove a dish from a menu
     * @param idMenu The menu ID
     * @param idDish The dish ID
     * @return Success message
     */
    @Tool(description = "Remove a dish from a menu")
    public String removeDishFromMenu(Long idMenu, Long idDish) {
        restClient.delete()
                .uri("/dish-menus?idmenu={idMenu}&iddish={idDish}", idMenu, idDish)
                .retrieve()
                .toBodilessEntity();
        return "Dish removed from menu successfully";
    }
}
