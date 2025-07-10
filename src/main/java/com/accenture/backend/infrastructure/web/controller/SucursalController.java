package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.dto.request.SucursalRequest;
import com.accenture.backend.application.service.interfaces.SucursalService;
import com.accenture.backend.domain.model.Sucursal;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> crearSucursal(@RequestBody SucursalRequest sucursal) {
        return sucursalService.crearSucursal(sucursal);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sucursal>> obtenerSucursalPorId(@PathVariable("id") String id) {
        return sucursalService.obtenerSucursalPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarSucursalPorId(@PathVariable("id") String id) {
        return sucursalService.eliminarSucursalPorId(id);
    }

    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Sucursal>> actualizarNombreSucursal(
            @PathVariable("id")  String id,
            @RequestBody String nuevoNombre) {
        return sucursalService.actualizarSucursalNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping
    public Flux<Sucursal> obtenerSucursales() {
        return sucursalService.obtenerSucursales();
    }



}