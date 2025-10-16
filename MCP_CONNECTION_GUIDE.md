# Guía de Conexión del Servidor MCP

## Visión General de la Arquitectura

Este servidor MCP utiliza el transporte **STDIO** (Entrada/Salida Estándar) para comunicarse con Claude Desktop. El servidor está configurado como una **aplicación no-web** que se comunica mediante flujos estándar.

### Configuración Clave

```properties
# STDIO Transport
spring.ai.mcp.server.stdio=true
spring.main.web-application-type=none

# All console logging disabled (required for clean JSON-RPC over STDIO)
logging.level.root=OFF
logging.level.org.springframework=OFF
logging.level.com.uptc.frw=OFF
```

Cuando Claude Desktop ejecuta el JAR:
- Envía mensajes JSON-RPC a **STDIN**
- Recibe respuestas JSON-RPC limpias desde **STDOUT**
- Todo el logging está suprimido para prevenir corrupción del protocolo

## Instrucciones de Configuración

### Paso 1: Construir el Servidor

Asegúrate de tener la última versión construida:

```bash
cd /Users/javiersandoval/Documents/Projects/MCPRestaurant
./gradlew clean build
```

### Paso 2: Configurar Claude Desktop para STDIO

Actualiza tu archivo de configuración MCP de Claude Desktop:

**macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "restaurant": {
      "command": "/usr/bin/java",
      "args": [
        "-jar",
        "/ruta/a/mcprestaurant-0.0.1-SNAPSHOT.jar"
      ],
      "env": {
        "RESTAURANT_API_BASE_URL": "http://localhost:8080/api",
        "RESTAURANT_API_USERNAME": "admin",
        "RESTAURANT_API_PASSWORD": "password"
      }
    }
  }
}
```

### Paso 3: Reiniciar Claude Desktop

1. Cierra Claude Desktop completamente
2. Lanza Claude Desktop
3. El servidor se iniciará automáticamente cuando sea necesario

## Verificar Conexión

Una vez conectado, deberías ver tus herramientas MCP disponibles en Claude Desktop:
- **Operaciones de Menús:** `getAllMenus`, `getMenuById`, `createMenu`, `updateMenu`, `deleteMenu`
- **Operaciones de Platos:** `getAllDishes`, `getDishById`, `createDish`, `updateDish`, `deleteDish`
- **Operaciones de Platos-Menús:** `getAllDishMenus`, `getDishMenu`, `addDishToMenu`, `updateDishMenu`, `removeDishFromMenu`
- **Operaciones de Ventas:** `getAllSales`, `getSaleById`, `createSale`, `updateSale`, `deleteSale`
- **Operaciones de Ventas-Menús:** `getAllSaleMenus`, `getMenusBySaleId`, `getSaleMenu`, `addMenuToSale`, `updateSaleMenu`, `removeMenuFromSale`

## Cómo Funciona el Transporte STDIO

1. **Claude Desktop lanza el JAR** cuando necesita usar el servidor MCP
2. **El servidor inicia silenciosamente** con todo el logging deshabilitado
3. **Comunicación mediante flujos estándar:**
   - Claude envía solicitudes JSON-RPC a STDIN
   - El servidor responde con mensajes JSON-RPC a STDOUT
4. **El servidor se apaga** cuando Claude Desktop ya no lo necesita

## Notas Importantes

### El Logging está Deshabilitado
Todo el logging de consola está desactivado para prevenir corromper el protocolo JSON-RPC:
```properties
logging.level.root=OFF
logging.level.org.springframework=OFF
logging.level.com.uptc.frw=OFF
```

### Sin Servidor Web
La aplicación se ejecuta como una aplicación no-web:
```properties
spring.main.web-application-type=none
```

Esto significa:
- ❌ No hay endpoints HTTP expuestos
- ❌ No hay enlace de puerto (server.port es ignorado)
- ✅ Comunicación ligera solo mediante STDIO
- ✅ Claude Desktop gestiona el ciclo de vida

## Solución de Problemas

### El servidor no aparece en Claude Desktop
1. Verifica los logs de Claude Desktop:
   - **macOS:** `~/Library/Logs/Claude/mcp*.log`
2. Verifica que la ruta del JAR en `claude_desktop_config.json` sea correcta
3. Asegúrate de reconstruir después de cualquier cambio en el código:
   ```bash
   ./gradlew clean build
   ```

### Errores de "Server disconnected"
- Verifica que tu API externa de restaurante esté ejecutándose en `http://localhost:8080/api`
- Verifica que las credenciales de la API (username/password) sean correctas
- Revisa los logs de Claude Desktop para mensajes de error detallados

### Depurando el Servidor
Dado que todo el logging está deshabilitado para STDIO, no puedes ver la salida de consola. Para depurar:

1. **Habilita temporalmente el logging a archivo** en `application.properties`:
   ```properties
   # Temporarily enable for debugging
   logging.level.root=DEBUG
   logging.file.name=/tmp/mcp-restaurant-debug.log
   ```

2. **Reconstruir y probar:**
   ```bash
   ./gradlew clean build
   ```

3. **Revisar el archivo de log:**
   ```bash
   tail -f /tmp/mcp-restaurant-debug.log
   ```

4. **Recuerda deshabilitar el logging nuevamente** cuando termines de depurar (restaura `logging.level.root=OFF`)

### Reconstruir Después de Cambios
Siempre reconstruye después de modificar el código:
```bash
./gradlew clean build
```

Luego reinicia Claude Desktop para cargar el nuevo JAR.

## Formato de Endpoints de la API

El servidor se comunica con tu API externa de restaurante usando los siguientes patrones:

### Operaciones de Entidad Única
- **GET:** `GET /api/{entity}` - Listar todos
- **GET por ID:** `GET /api/{entity}/{id}` - Obtener uno
- **POST:** `POST /api/{entity}` con body JSON
- **PUT:** `PUT /api/{entity}` con body JSON (ID en el body)
- **DELETE:** `DELETE /api/{entity}?id={id}` - Parámetro de consulta

### Operaciones de Relación (DishMenu, SaleMenu)
- **GET:** `GET /api/{relation}?idmenu={id}&iddish={id}` - Parámetros de consulta
- **POST:** `POST /api/{relation}` con body JSON
- **PUT:** `PUT /api/{relation}` con body JSON (todos los IDs en el body)
- **DELETE:** `DELETE /api/{relation}?idmenu={id}&iddish={id}` - Parámetros de consulta

## Referencia de Configuración

### Configuración Actual (`application.properties`)

```properties
# Banner and logging
spring.main.banner-mode=off
logging.level.root=OFF
logging.level.org.springframework=OFF  
logging.level.com.uptc.frw=OFF

# MCP Server - STDIO Transport
spring.ai.mcp.server.name=mcprestaurant
spring.ai.mcp.server.version=0.0.1
spring.ai.mcp.server.stdio=true
spring.main.web-application-type=none

# Change notifications
spring.ai.mcp.server.resource-change-notification=true
spring.ai.mcp.server.tool-change-notification=true
spring.ai.mcp.server.prompt-change-notification=true

# External Restaurant API
restaurant.api.base-url=http://localhost:8080/api
restaurant.api.username=admin
restaurant.api.password=password
```

### Variables de Entorno (Producción)

En lugar de codificar las credenciales directamente, usa variables de entorno:

```bash
export RESTAURANT_API_BASE_URL=https://your-api.com/api
export RESTAURANT_API_USERNAME=your_username
export RESTAURANT_API_PASSWORD=your_password
```

Luego configura en Claude Desktop:
```json
{
  "mcpServers": {
    "restaurant": {
      "command": "/usr/bin/java",
      "args": ["-jar", "/path/to/mcprestaurant-0.0.1-SNAPSHOT.jar"],
      "env": {
        "RESTAURANT_API_BASE_URL": "https://your-api.com/api",
        "RESTAURANT_API_USERNAME": "your_username",
        "RESTAURANT_API_PASSWORD": "your_password"
      }
    }
  }
}
```
