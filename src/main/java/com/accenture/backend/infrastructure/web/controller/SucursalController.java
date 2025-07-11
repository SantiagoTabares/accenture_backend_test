package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.dto.request.SucursalRequest;
import com.accenture.backend.application.service.interfaces.SucursalService;
import com.accenture.backend.domain.model.Sucursal;
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
@RequestMapping("/api/v1/sucursal")
@RequiredArgsConstructor
@Tag(name = "Sucursal", description = "Operaciones relacionadas con sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    @Operation(summary = "Crear una nueva sucursal")
    @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> crearSucursal(@RequestBody SucursalRequest sucursal) {
        return sucursalService.crearSucursal(sucursal);
    }

    @Operation(summary = "Obtener una sucursal por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sucursal>> obtenerSucursalPorId(@PathVariable("id") String id) {
        return sucursalService.obtenerSucursalPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una sucursal por su ID")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarSucursalPorId(@PathVariable("id") String id) {
        return sucursalService.eliminarSucursalPorId(id);
    }

    @Operation(summary = "Actualizar el nombre de una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de sucursal actualizado"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Sucursal>> actualizarNombreSucursal(
            @PathVariable("id") String id,
            @RequestBody String nuevoNombre) {
        return sucursalService.actualizarSucursalNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todas las sucursales")
    @ApiResponse(responseCode = "200", description = "Listado de sucursales")
    @GetMapping
    public Flux<Sucursal> obtenerSucursales() {
        return sucursalService.obtenerSucursales();
    }

}
