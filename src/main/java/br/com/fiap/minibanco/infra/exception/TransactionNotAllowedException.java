package br.com.fiap.minibanco.infra.exception;

public class TransactionNotAllowedException extends RuntimeException {
    public TransactionNotAllowedException(String message) {
        super(message);
    }

    public TransactionNotAllowedException() {
        super("Sua transação foi negada por motivos de segurança, tente novamente em alguns minutos");
    }
}
