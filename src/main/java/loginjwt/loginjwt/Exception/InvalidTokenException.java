package loginjwt.loginjwt.Exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token inválido ou já utilizado");
    }
}
