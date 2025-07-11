package com.accenture.backend.application.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class FranquiciaRequest {
    private String nombre;
    private List<String> sucursalesIds;
}