package com.accenture.backend.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoMaxResponse {
    private String id;
    private String nombre;
    private int stock;
}