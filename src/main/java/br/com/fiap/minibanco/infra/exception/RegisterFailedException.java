package br.com.fiap.minibanco.infra.exception;

public class RegisterFailedException extends RuntimeException {
    public RegisterFailedException(String message) {
        super(message);
    }

    public RegisterFailedException() {
        super("Erro no registro, usuário já cadastrado");
    }
}
