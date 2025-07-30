package br.com.fiap.minibanco.infra.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException()
  {
    super("Esse cpf já existe, tente novamente com um cpf diferente");
  }

  public UserNotFoundException(String cpf)
  {
    super("O cpf: "+ cpf + " não existe, por favor informe um cpf válido! ");
  }
}
