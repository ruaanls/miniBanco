package br.com.fiap.minibanco.infra.exception;

public class OracleInputException extends RuntimeException {
  public OracleInputException(String message) {
    super(message);
  }

  public OracleInputException() {
    super("Erro de inconsistência de dados, tente novamente em alguns minutos");
  }
}
