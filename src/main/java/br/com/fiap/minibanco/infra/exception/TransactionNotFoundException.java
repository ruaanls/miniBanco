package br.com.fiap.minibanco.infra.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Não existe nenhuma transação com o id: "+ id+ " em nosso sistema, por favor tente novamente com um outro id");
    }
}
