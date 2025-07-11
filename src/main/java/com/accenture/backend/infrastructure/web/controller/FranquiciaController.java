package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.dto.request.FranquiciaRequest;
import com.accenture.backend.application.dto.response.SucursalConProductoMaxResponse;
import com.accenture.backend.application.service.interfaces.FranquiciaService;
import com.accenture.backend.domain.model.Franquicia;
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
@RequestMapping("/api/v1/franquicia")
@RequiredArgsConstructor
@Tag(name = "Franquicia", description = "Operaciones relacionadas con franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @Operation(summary = "Crear una nueva franquicia")
    @ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> crearFranquicia(@RequestBody FranquiciaRequest franquicia) {
        return franquiciaService.crearFranquicia(franquicia);
    }

    @Operation(summary = "Obtener una franquicia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franquicia encontrada"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Franquicia>> obtenerFranquiciaPorId(@PathVariable("id") String id) {
        return franquiciaService.obtenerFranquiciaPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una franquicia por su ID")
    @ApiResponse(responseCode = "204", description = "Franquicia eliminada exitosamente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarFranquiciaPorId(@PathVariable("id") String id) {
        return franquiciaService.eliminarFranquiciaPorId(id);
    }

    @Operation(summary = "Actualizar el nombre de una franquicia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de franquicia actualizado"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Franquicia>> actualizarNombreFranquicia(
            @PathVariable("id") String id,
            @RequestBody String nuevoNombre) {
        return franquiciaService.actualizarFranquiciaNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todas las franquicias")
    @ApiResponse(responseCode = "200", description = "Listado de franquicias")
    @GetMapping
    public Flux<Franquicia> obtenerFranquicias() {
        return franquiciaService.obtenerFranquicias();
    }

    @Operation(summary = "Obtener el producto con más stock por sucursal para una franquicia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de sucursales con producto de mayor stock")
    })
    @GetMapping("/franquicia/{id}/sucursales-producto-max")
    public Flux<SucursalConProductoMaxResponse> obtenerProductosMaxPorSucursal(@PathVariable("id") String id) {
        return franquiciaService.obtenerProductosConMasStockPorSucursal(id);
    }

}