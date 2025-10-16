package com.uptc.frw.mcprestaurant;

import com.uptc.frw.mcprestaurant.service.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MCP Server Application for Restaurant Management
 * 
 * This application exposes restaurant management tools through the Model Context Protocol (MCP).
 * It provides CRUD operations for menus, dishes, sales, and their relationships.
 */
@SpringBootApplication
public class McprestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(McprestaurantApplication.class, args);
	}

	/**
	 * Register menu management tools
	 */
	@Bean
	public ToolCallbackProvider menuTools(MenuService menuService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(menuService)
				.build();
	}

	/**
	 * Register dish management tools
	 */
	@Bean
	public ToolCallbackProvider dishTools(DishService dishService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(dishService)
				.build();
	}

	/**
	 * Register dish-menu relationship tools
	 */
	@Bean
	public ToolCallbackProvider dishMenuTools(DishMenuService dishMenuService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(dishMenuService)
				.build();
	}

	/**
	 * Register sale management tools
	 */
	@Bean
	public ToolCallbackProvider saleTools(SaleService saleService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(saleService)
				.build();
	}

	/**
	 * Register sale-menu relationship tools
	 */
	@Bean
	public ToolCallbackProvider saleMenuTools(SaleMenuService saleMenuService) {
		return MethodToolCallbackProvider.builder()
				.toolObjects(saleMenuService)
				.build();
	}
}
