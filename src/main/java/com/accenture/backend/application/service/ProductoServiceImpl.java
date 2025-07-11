package com.accenture.backend.application.service;

import com.accenture.backend.application.service.interfaces.ProductoService;
import com.accenture.backend.domain.model.Producto;
import com.accenture.backend.domain.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public Mono<Producto> crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Mono<Producto> obtenerProductoPorId(String id) {
        return productoRepository.findById(id);
    }

    @Override
    public Mono<Void> eliminarProductoPorId(String id) {
        return productoRepository.deleteById(id);
    }

    @Override
    public Mono<Producto> actualizarProductoNombre(String id, String productoNombre) {
        return productoRepository.findById(id)
                .flatMap(producto -> {
                    producto.setNombre(productoNombre);
                    return productoRepository.save(producto);
                });
    }

    @Override
    public Flux<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

}