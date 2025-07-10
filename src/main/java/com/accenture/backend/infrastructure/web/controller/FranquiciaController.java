package com.accenture.backend.infrastructure.web.controller;

import com.accenture.backend.application.dto.request.FranquiciaRequest;
import com.accenture.backend.application.service.interfaces.FranquiciaService;
import com.accenture.backend.domain.model.Franquicia;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> crearFranquicia(@RequestBody FranquiciaRequest franquicia) {
        return franquiciaService.crearFranquicia(franquicia);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Franquicia>> obtenerFranquiciaPorId(@PathVariable("id") String id) {
        return franquiciaService.obtenerFranquiciaPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarFranquiciaPorId(@PathVariable("id") String id) {
        return franquiciaService.eliminarFranquiciaPorId(id);
    }

    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<Franquicia>> actualizarNombreFranquicia(
            @PathVariable("id")  String id,
            @RequestBody String nuevoNombre) {
        return franquiciaService.actualizarFranquiciaNombre(id, nuevoNombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping
    public Flux<Franquicia> obtenerFranquicias() {
        return franquiciaService.obtenerFranquicias();
    }



}