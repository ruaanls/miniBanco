package br.com.fiap.minibanco.infra.exception;

public class EmailExistsException extends RuntimeException {

  public EmailExistsException(String email)
  {
    super("O email: "+email+" está em uso, por favor tente outro email ou realize o login");
  }
}
