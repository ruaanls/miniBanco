package br.com.fiap.minibanco.infra.exception;

public class SameCPFException extends RuntimeException {
    public SameCPFException(String message) {
        super(message);
    }

    public SameCPFException() {
        super("Não é possível fazer uma transferência para o mesmo cpf, por favor tente novamente com um outro cpf");
    }
}
