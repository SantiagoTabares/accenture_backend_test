package com.accenture.backend.application.service.interfaces;


import com.accenture.backend.application.dto.request.FranquiciaRequest;
import com.accenture.backend.application.dto.response.ProductoConSucursalResponse;
import com.accenture.backend.domain.model.Franquicia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaService {
     Mono<Franquicia> crearFranquicia(FranquiciaRequest franquicia);
     Mono<Franquicia> obtenerFranquiciaPorId(String id);
     Mono<Void> eliminarFranquiciaPorId(String id);
     Mono<Franquicia> actualizarFranquiciaNombre(String id, String FranquiciaNombre);
     Flux<Franquicia> obtenerFranquicias();
     Flux<ProductoConSucursalResponse> obtenerProductosConMasStockPorSucursal(String franquiciaId);
}
