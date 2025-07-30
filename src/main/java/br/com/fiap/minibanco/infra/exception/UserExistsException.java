package br.com.fiap.minibanco.infra.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException() {
        super("usuário já cadastrado, por favor tente novamente com um outro cpf");
    }
}
