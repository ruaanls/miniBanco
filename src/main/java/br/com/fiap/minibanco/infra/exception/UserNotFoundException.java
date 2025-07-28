package br.com.fiap.minibanco.infra.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException()
  {
    super("Esse usuário não existe, por favor informe um login válido! ");
  }
}
