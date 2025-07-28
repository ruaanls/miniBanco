package br.com.fiap.minibanco.infra.exception;

public class OracleInputException extends RuntimeException {
  public OracleInputException(String message) {
    super(message);
  }
}
