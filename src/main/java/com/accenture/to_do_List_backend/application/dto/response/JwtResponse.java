package com.accenture.to_do_List_backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "JWT response after successful authentication")
public class JwtResponse {

    @Schema(description = "Generated JWT token", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private String token;
}