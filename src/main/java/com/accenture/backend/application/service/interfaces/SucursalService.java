package com.accenture.backend.application.service.interfaces;


import com.accenture.backend.application.dto.request.SucursalRequest;
import com.accenture.backend.domain.model.Producto;
import com.accenture.backend.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalService {


    Mono<Sucursal> crearSucursal(SucursalRequest sucursalRequest);
    Mono<Sucursal> obtenerSucursalPorId(String id);
    Mono<Void> eliminarSucursalPorId(String id);
    Mono<Sucursal> actualizarSucursalNombre(String id, String sucursal);
    Flux<Sucursal> obtenerSucursales();


}
