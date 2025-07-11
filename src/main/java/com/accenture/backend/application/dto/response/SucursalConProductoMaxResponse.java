package com.accenture.backend.application.dto.response;

import com.accenture.backend.domain.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SucursalConProductoMaxResponse {
    private String id;
    private String nombre;
    private Producto productoMax;
}
