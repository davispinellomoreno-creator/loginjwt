package loginjwt.loginjwt.Dto;

public record LoginResponse(String token, String tipo) {
    public LoginResponse(String token) {
        this(token, "Bearer");
    }
}
