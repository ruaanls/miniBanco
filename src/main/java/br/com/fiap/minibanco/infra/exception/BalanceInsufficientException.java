package br.com.fiap.minibanco.infra.exception;

public class BalanceInsufficientException extends RuntimeException {
    public BalanceInsufficientException(String message) {
        super(message);
    }
    public BalanceInsufficientException() {
        super("Sua transferência foi NEGADA, por saldo insuficiente. Por favor verifique seu saldo atual disponível e tente novamente");
    }
}
