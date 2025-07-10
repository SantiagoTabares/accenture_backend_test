package com.accenture.backend.application.service.interfaces;


import com.accenture.backend.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Mono<Producto> crearProducto(Producto producto);
    Mono<Producto> obtenerProductoPorId(String id);
    Mono<Void> eliminarProductoPorId(String id);
    Mono<Producto> actualizarProductoNombre(String id, String productoNombre);
    Flux<Producto> obtenerProductos();

}
