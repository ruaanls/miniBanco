package br.com.fiap.minibanco.infra.adapters.outbound.security;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;

import java.time.Instant;

public interface TokenServicePort
{
    String generateToken(UserJpa userJpa);
    String validateToken(String token);
    Instant genExpirationDate();
}
