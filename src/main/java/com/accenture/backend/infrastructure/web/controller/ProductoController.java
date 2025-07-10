package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.service.interfaces.ProductoService;
import com.accenture.backend.domain.model.Producto;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> obtenerProductoPorId(@PathVariable("id") String id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProductoPorId(@PathVariable("id") String id) {
        return productoService.eliminarProductoPorId(id);
    }

    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Producto>> actualizarNombreProducto(
            @PathVariable("id")  String id,
            @RequestBody String nuevoNombre) {
        return productoService.actualizarProductoNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Producto> findAll() {
        return productoService.obtenerProductos();
    }



}