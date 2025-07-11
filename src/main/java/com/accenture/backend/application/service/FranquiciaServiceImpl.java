package com.accenture.backend.application.service;

import com.accenture.backend.application.dto.request.FranquiciaRequest;

import com.accenture.backend.application.dto.response.SucursalConProductoMaxResponse;

import com.accenture.backend.application.service.interfaces.FranquiciaService;
import com.accenture.backend.domain.model.Franquicia;

import com.accenture.backend.domain.model.Producto;
import com.accenture.backend.domain.repository.FranquiciaRepository;
import com.accenture.backend.domain.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    @Override
    public Mono<Franquicia> crearFranquicia(FranquiciaRequest request) {
        List<String> ids = request.getSucursalesIds();

        return Flux.fromIterable(ids)
                .flatMap(id -> sucursalRepository.findById(id)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sucursal no encontrado con ID: " + id)))
                )
                .collectList()
                .flatMap(sucursalesEncontradas -> {
                    Franquicia franquicia = new Franquicia();
                    franquicia.setNombre(request.getNombre());
                    franquicia.setSucursales(sucursalesEncontradas);
                    return franquiciaRepository.save(franquicia);
                });
    }

    @Override
    public Mono<Franquicia> obtenerFranquiciaPorId(String id) {
        return franquiciaRepository.findById(id);
    }

    @Override
    public Mono<Void> eliminarFranquiciaPorId(String id) {
        return franquiciaRepository.deleteById(id);
    }

    @Override
    public Mono<Franquicia> actualizarFranquiciaNombre(String id, String FranquiciaNombre) {
        return franquiciaRepository.findById(id)
                .flatMap(Franquicia -> {
                    Franquicia.setNombre(FranquiciaNombre);
                    return franquiciaRepository.save(Franquicia);
                });
    }

    @Override
    public Flux<Franquicia> obtenerFranquicias() {
        return franquiciaRepository.findAll();
    }


    public Flux<SucursalConProductoMaxResponse> obtenerProductosConMasStockPorSucursal(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
                .map(sucursal -> {
                    Producto productoMax = sucursal.getProductos().stream()
                            .max(Comparator.comparingInt(Producto::getStock))
                            .orElse(null);

                    return new SucursalConProductoMaxResponse(
                            sucursal.getId(),
                            sucursal.getNombre(),
                            productoMax
                    );
                });
    }
}