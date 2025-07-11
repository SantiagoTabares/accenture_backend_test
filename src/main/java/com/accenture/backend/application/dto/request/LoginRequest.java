package com.accenture.backend.application.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Login credentials")
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "Username of the user", example = "admin")
    private String username;

    @Schema(description = "Password of the user", example = "1234")
    private String password;
}