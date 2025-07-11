package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.service.interfaces.ProductoService;
import com.accenture.backend.domain.model.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Producto", description = "Operaciones relacionadas con productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> obtenerProductoPorId(@PathVariable("id") String id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar producto por ID")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProductoPorId(@PathVariable("id") String id) {
        return productoService.eliminarProductoPorId(id);
    }

    @Operation(summary = "Actualizar el nombre del producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Producto>> actualizarNombreProducto(
            @PathVariable("id") String id,
            @RequestBody String nuevoNombre) {
        return productoService.actualizarProductoNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public Flux<Producto> findAll() {
        return productoService.obtenerProductos();
    }

}