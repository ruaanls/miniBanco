package br.com.fiap.minibanco.infra.adapters.outbound.security;

import br.com.fiap.minibanco.infra.adapters.outbound.persistence.entities.UserJpa;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceImpl implements TokenServicePort
{
    @Value("${JWT_SECRET:my-secret-key}")
    private String secret;

    @Override
    public String generateToken(UserJpa userJpa) {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("apisecurity")
                    .withSubject(userJpa.getCpf())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }
        catch (JWTCreationException e)
        {
            throw new RuntimeException("Erro ao gerar token: ", e.getCause());
        }
    }




    @Override
    public String validateToken(String token) {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("apisecurity")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (TokenExpiredException e)
        {
            throw new TokenExpiredException("Token JWT Expirado por favor, realize um login novamente", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token JWT inválido por favor, realize um login novamente ", e.getCause());
        }
    }

    @Override
    public Instant genExpirationDate() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()));
    }


}