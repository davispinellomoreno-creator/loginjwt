package loginjwt.loginjwt.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String newPassword
) {}
