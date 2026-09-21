package loginjwt.loginjwt.Exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token expirado. Solicite um novo");
    }
}
