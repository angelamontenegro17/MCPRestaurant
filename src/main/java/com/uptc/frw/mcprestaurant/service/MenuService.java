package com.uptc.frw.mcprestaurant.service;

import com.uptc.frw.mcprestaurant.model.Menu;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Service for managing restaurant menus
 */
@Service
public class MenuService {

    private final RestClient restClient;

    public MenuService(@Value("${restaurant.api.base-url:http://localhost:8080/api}") String baseUrl,
                      @Value("${restaurant.api.username:admin}") String username,
                      @Value("${restaurant.api.password:password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    /**
     * Get all menus from the restaurant
     * @return List of all menus
     */
    @Tool(description = "Get all menus from the restaurant")
    public List<Menu> getAllMenus() {
        return restClient.get()
                .uri("/menus")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Menu>>() {});
    }

    /**
     * Get a specific menu by ID
     * @param idMenu The menu ID
     * @return The menu details
     */
    @Tool(description = "Get a specific menu by its ID")
    public Menu getMenuById(Long idMenu) {
        return restClient.get()
                .uri("/menus/{id}", idMenu)
                .retrieve()
                .body(Menu.class);
    }

    /**
     * Create a new menu
     * @param description The menu description
     * @return The created menu
     */
    @Tool(description = "Create a new menu with a description")
    public Menu createMenu(String description) {
        return restClient.post()
                .uri("/menus")
                .body(Map.of("description", description))
                .retrieve()
                .body(Menu.class);
    }

    /**
     * Update an existing menu
     * @param idMenu The menu ID
     * @param description The new description
     * @return The updated menu
     */
    @Tool(description = "Update an existing menu's description")
    public Menu updateMenu(Long idMenu, String description) {
        return restClient.put()
                .uri("/menus")
                .body(Map.of(
                    "idMenu", idMenu,
                    "description", description
                ))
                .retrieve()
                .body(Menu.class);
    }

    /**
     * Delete a menu
     * @param idMenu The menu ID to delete
     * @return Success message
     */
    @Tool(description = "Delete a menu by its ID")
    public String deleteMenu(Long idMenu) {
        restClient.delete()
                .uri("/menus?id={idMenu}", idMenu)
                .retrieve()
                .toBodilessEntity();
        return "Menu deleted successfully";
    }
}
