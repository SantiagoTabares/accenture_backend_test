package com.accenture.backend.application.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SucursalRequest {
    private String nombre;
    private List<String> productoIds;
}