package loginjwt.loginjwt.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Usuário com email '" + email + "' não encontrado");
    }
}
