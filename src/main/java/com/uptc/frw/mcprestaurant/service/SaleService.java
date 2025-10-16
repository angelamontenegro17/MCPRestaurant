package com.uptc.frw.mcprestaurant.service;

import com.uptc.frw.mcprestaurant.model.Sale;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Service for managing restaurant sales
 */
@Service
public class SaleService {

    private final RestClient restClient;

    public SaleService(@Value("${restaurant.api.base-url:http://localhost:8080/api}") String baseUrl,
                      @Value("${restaurant.api.username:admin}") String username,
                      @Value("${restaurant.api.password:password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    /**
     * Get all sales from the restaurant
     * @return List of all sales
     */
    @Tool(description = "Get all sales from the restaurant")
    public List<Sale> getAllSales() {
        return restClient.get()
                .uri("/sales")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Sale>>() {});
    }

    /**
     * Get a specific sale by ID
     * @param id The sale ID
     * @return The sale details
     */
    @Tool(description = "Get a specific sale by its ID")
    public Sale getSaleById(Long id) {
        return restClient.get()
                .uri("/sales/{id}", id)
                .retrieve()
                .body(Sale.class);
    }

    /**
     * Create a new sale
     * @param date The sale date
     * @return The created sale
     */
    @Tool(description = "Create a new sale record")
    public Sale createSale(String date) {
        return restClient.post()
                .uri("/sales")
                .body(Map.of("date", date))
                .retrieve()
                .body(Sale.class);
    }

    /**
     * Update an existing sale
     * @param id The sale ID
     * @param date The new date
     * @return The updated sale
     */
    @Tool(description = "Update an existing sale's date")
    public Sale updateSale(Long id, String date) {
        return restClient.put()
                .uri("/sales")
                .body(Map.of(
                    "id", id,
                    "date", date
                ))
                .retrieve()
                .body(Sale.class);
    }

    /**
     * Delete a sale
     * @param id The sale ID to delete
     * @return Success message
     */
    @Tool(description = "Delete a sale by its ID")
    public String deleteSale(Long id) {
        restClient.delete()
                .uri("/sales?id={id}", id)
                .retrieve()
                .toBodilessEntity();
        return "Sale deleted successfully";
    }
}
