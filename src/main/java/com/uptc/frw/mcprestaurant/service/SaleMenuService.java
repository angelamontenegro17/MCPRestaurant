package com.uptc.frw.mcprestaurant.service;

import com.uptc.frw.mcprestaurant.model.SaleMenu;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Service for managing sale-menu relationships
 */
@Service
public class SaleMenuService {

    private final RestClient restClient;

    public SaleMenuService(@Value("${restaurant.api.base-url:http://localhost:8080/api}") String baseUrl,
                          @Value("${restaurant.api.username:admin}") String username,
                          @Value("${restaurant.api.password:password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    /**
     * Get all sale-menu relationships
     * @return List of all sale-menu relationships
     */
    @Tool(description = "Get all sale-menu relationships")
    public List<SaleMenu> getAllSaleMenus() {
        return restClient.get()
                .uri("/SalesMenu")
                .retrieve()
                .body(new ParameterizedTypeReference<List<SaleMenu>>() {});
    }

    /**
     * Get all menus for a specific sale
     * @param idSale The sale ID
     * @return List of menus in the sale
     */
    @Tool(description = "Get all menus for a specific sale")
    public List<SaleMenu> getMenusBySaleId(Long idSale) {
        return restClient.get()
                .uri("/SalesMenu/sale/{idSale}", idSale)
                .retrieve()
                .body(new ParameterizedTypeReference<List<SaleMenu>>() {});
    }

    /**
     * Get a specific sale-menu relationship
     * @param idMenu The menu ID
     * @param idSale The sale ID
     * @return The sale-menu relationship
     */
    @Tool(description = "Get a specific sale-menu relationship")
    public SaleMenu getSaleMenu(Long idMenu, Long idSale) {
        return restClient.get()
                .uri("/SalesMenu/{menuId}/{saleId}", idMenu, idSale)
                .retrieve()
                .body(SaleMenu.class);
    }

    /**
     * Add a menu to a sale
     * @param menuId The menu ID
     * @param saleId The sale ID
     * @param quantity The quantity of this menu in the sale
     * @return The created relationship
     */
    @Tool(description = "Add a menu to a sale with quantity")
    public SaleMenu addMenuToSale(Long idMenu, Long idSale, Integer quantity) {
        return restClient.post()
                .uri("/SalesMenu")
                .body(Map.of(
                    "menuId", idMenu,
                    "saleId", idSale,
                    "quantity", quantity
                ))
                .retrieve()
                .body(SaleMenu.class);
    }

    /**
     * Update a sale-menu relationship
     * @param idMenu The menu ID
     * @param idSale The sale ID
     * @param quantity The new quantity
     * @return The updated relationship
     */
    @Tool(description = "Update the quantity of a menu in a sale")
    public SaleMenu updateSaleMenu(Long idMenu, Long idSale, Integer quantity) {
        return restClient.put()
                .uri("/SalesMenu")
                .body(Map.of(
                    "idMenu", idMenu,
                    "idSale", idSale,
                    "quantity", quantity
                ))
                .retrieve()
                .body(SaleMenu.class);
    }

    /**
     * Remove a menu from a sale
     * @param idMenu The menu ID
     * @param idSale The sale ID
     * @return Success message
     */
    @Tool(description = "Remove a menu from a sale")
    public String removeMenuFromSale(Long idMenu, Long idSale) {
        restClient.delete()
                .uri("/SalesMenu?idmenu={idMenu}&idsale={idSale}", idMenu, idSale)
                .retrieve()
                .toBodilessEntity();
        return "Menu removed from sale successfully";
    }
}
