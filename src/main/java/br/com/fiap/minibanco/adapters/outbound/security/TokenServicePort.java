package br.com.fiap.minibanco.adapters.outbound.security;

import br.com.fiap.minibanco.adapters.outbound.JPA.entities.UserJpa;

import java.time.Instant;

public interface TokenServicePort
{
    String generateToken(UserJpa userJpa);
    String validateToken(String token);
    Instant genExpirationDate();
}
