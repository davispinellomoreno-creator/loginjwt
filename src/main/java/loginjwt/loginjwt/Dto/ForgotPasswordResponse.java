package loginjwt.loginjwt.Dto;

public record ForgotPasswordResponse(
        String message,
        String resetToken
) {}
