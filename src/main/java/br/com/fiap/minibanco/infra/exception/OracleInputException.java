package br.com.fiap.minibanco.infra.exception;


public class OracleInputException extends RuntimeException {
  public OracleInputException(String message, Throwable causa) {
    super(message, causa);
  }

  public OracleInputException(Throwable Causa) {
    super("Erro de inconsistência de dados, tente novamente em alguns minutos  \n\n ",Causa );
  }

  public OracleInputException() {
    super("Erro de inconsistência de dados, tente novamente em alguns minutos");
  }
}
