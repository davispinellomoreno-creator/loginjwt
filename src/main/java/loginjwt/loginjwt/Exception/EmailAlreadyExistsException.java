package loginjwt.loginjwt.Exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("O email '" + email + "' já está cadastrado");
    }
}
