package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.SucursalRequest;
import com.accenture.backend.application.dto.request.TaskRequest;
import com.accenture.backend.application.dto.response.CategoryResponse;
import com.accenture.backend.application.dto.response.TaskResponse;
import com.accenture.backend.application.service.interfaces.SucursalService;
import com.accenture.backend.application.service.interfaces.SucursalService;
import com.accenture.backend.domain.model.Franchise;
import com.accenture.backend.domain.model.Producto;
import com.accenture.backend.domain.model.Sucursal;
import com.accenture.backend.domain.model.Sucursal;
import com.accenture.backend.domain.repository.ProductoRepository;
import com.accenture.backend.domain.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Mono<Sucursal> crearSucursal(SucursalRequest request) {
        List<String> ids = request.getProductoIds();

        return Flux.fromIterable(ids)
                .flatMap(id -> productoRepository.findById(id)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado con ID: " + id)))
                )
                .collectList()
                .flatMap(productosEncontrados -> {
                    Sucursal sucursal = new Sucursal();
                    sucursal.setNombre(request.getNombre());
                    sucursal.setProductos(productosEncontrados);
                    return sucursalRepository.save(sucursal);
                });
    }

    @Override
    public Mono<Sucursal> obtenerSucursalPorId(String id) {
        return sucursalRepository.findById(id);
    }

    @Override
    public Mono<Void> eliminarSucursalPorId(String id) {
        return sucursalRepository.deleteById(id);
    }

    @Override
    public Mono<Sucursal> actualizarSucursalNombre(String id, String SucursalNombre) {
        return sucursalRepository.findById(id)
                .flatMap(Sucursal -> {
                    Sucursal.setNombre(SucursalNombre);
                    return sucursalRepository.save(Sucursal);
                });
    }

    public Flux<Sucursal> obtenerSucursales() {
        return sucursalRepository.findAll();
    }
}