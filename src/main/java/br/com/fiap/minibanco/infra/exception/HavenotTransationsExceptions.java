package br.com.fiap.minibanco.infra.exception;

public class HavenotTransationsExceptions extends RuntimeException {
    public HavenotTransationsExceptions(String message) {
        super(message);
    }

    public HavenotTransationsExceptions() {
        super("Não foram encontradas nenhuma transferência no CPF informado.");
    }
}
